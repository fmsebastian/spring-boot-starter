package com.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.utils.JsonUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * This class is an storage static class for methods that
 * @author fsebastian
 *
 */
public class JSONTemplateTransformer {
    /**
     * This method fills the gaps in <b>jsonTemplateMap</b> with the values of <b>values</b> map. It will go across the map transforming every object
     * and adding it to a new JSONObject that will be returned. Values that can not be filled for any reason will be dropped from the map.
     * @param jsonTemplateMap map to be filled out.
     * @param values to fill out the map.
     * @return A JSONObject representing the jsonFormatMap filled out with values.
     * @see #getObjectValue(String, Map)
     * @see #getJSONObjectAsObject(Object, Map)
     * @see #getJsonArray(ArrayList, Map)
     */
    public static JSONObject getJsonMap(Map<String, Object> jsonTemplateMap, Map<String, Object> values) {
        JSONObject fields = new JSONObject();
        jsonTemplateMap.forEach((k,v) -> {
            Object jsonObject = JSONTemplateTransformer.getJSONObjectAsObject(v, values);
            if (jsonObject != null)
                fields.put(k, jsonObject);
        });
        if (! fields.isEmpty())
            return fields;
        else
            return null;
    }
    /**
     * As {@link #getJsonMap(Map, Map)}, this method fills out the template json array with values in <b>values</b>.
     * It will go across the array, filling out the gaps with 'values' map. Values that can not be filled for any reason will be dropped from the array.
     * @param array to be filled out.
     * @param values to fill out the array objects.
     * @return A JSONArray representing the input one but filled out with the values.
     */
    public static JSONArray getJsonArray(ArrayList<Object> array, Map<String, Object> values) {
        JSONArray components = new JSONArray();
        if (array != null && !array.isEmpty()){
            Object arrayType = array.get(0);
            String[] splittedModel = ((String) arrayType).split(":");
            String arrayTypeKey = splittedModel[0];
            if (arrayTypeKey.equals("listOfBlockElements") && splittedModel.length == 2) {
                String key = splittedModel[1];
                Object valuesForList = values.get(key);
                if (valuesForList instanceof List) {
                    components = getJsonOpenArrayOfBlockElements(array.get(1), (List<Object>) valuesForList);
                }
            }
            else if (arrayTypeKey.equals("listOfSingleElements") && splittedModel.length == 2) {
                String key = splittedModel[1];
                Object valuesForList = values.get(key);
                if (valuesForList instanceof List) {
                    components = getJsonOpenArrayOfSingleElements((List<Object>) valuesForList);
                }
            }
            else {
                getJsonClosedArray(array, values);
            }
        }
        else {
            // LOG ERROR

        }
        if (! components.isEmpty())
            return components;
        else
            return null;
    }

    private static JSONArray getJsonOpenArrayOfSingleElements(List<Object> elemValues) {
        JSONArray components = new JSONArray();
        elemValues.forEach(elemValue -> {
            if (elemValue instanceof String){
                components.add(elemValue);
            }
        });
        return components;
    }
    private static JSONArray getJsonOpenArrayOfBlockElements(Object model, List<Object> elemValues){
        JSONArray components = new JSONArray();
        elemValues.forEach(elemValue -> {
            Object loadedElement;
            if (elemValue instanceof Map && model instanceof Map){
                loadedElement = getJsonMap( (Map<String, Object>) model, (Map<String, Object>) elemValue);
            }
            else {
                // Some error log
                System.out.println("Array definition is malformed, or list of values is not well received. ");
                loadedElement = null;
            }
            if (loadedElement != null)
                components.add(loadedElement);
        });
        return components;
    }

    private static JSONArray getJsonClosedArray(ArrayList<Object> array, Map<String, Object> values){
        JSONArray components = new JSONArray();
        array.forEach(obj -> {
            Object jsonObject = JSONTemplateTransformer.getJSONObjectAsObject(obj, values);
            if (jsonObject != null)
                components.add(jsonObject);
        });
        return components;
    }
    /**
     * This check the input String and select the way to transform it into a value
     * String must be of the form "keyword:keyvalue(:optionalValue)", and will be split by ':'.
     * <br>Options for keyword are:
     * <pre>
     * 		- substitute:	looks for keyvalue as key in values map, if it exists, returns the value for it as Object. Null if it does not exists.
     * 		- literal:	returns the keyvalue as Object unchanged. Null if input has not have a keyvalue.
     * 		- optional:	same as substitute, looks for keyvalue as key in values map, if it exists, returns the value for it as Object.
     * 				However, if it does not exists it will return an empty String ("").
     * 		- getOrDefault:	as substitute, this will return the value for keyvalue in values map if it exists.
     * 				If it does not exists, and there is an optionalValue in input, it will return optionalValue as Object. If any of those, it will return an empty string.
     * 		- append: It can have up to 4 parts (append:keyvalue:option1:option2). it returns the keyvalue as prefix of the result of
     * 				a) get the the value of option1 in the values map. If option1 does not exists then
     * 				b) get the option2 if exists. If option 2 does not exists then
     * 				c) returns the keyvalue itself.
     * </pre>
     * If the keyword is neither of the above, it will return a null object.
     * @param input String to be split and transform.
     * @param values Map to get the value for the String if needed.
     * @return The result String of transforming input. Null is the transform keyword is not one of the above (or in substitute cases).
     */
    public static Object getObjectValue(String input, Map<String, Object> values){
        Object jsonObject = null;
        String[] parts = input.split(":");
        switch (parts[0]) {
            case "substitute"	:
                if (values.containsKey(parts[1])){
                    jsonObject = (Object) values.get(parts[1]);
                }
                break;
            case "literal"		:
                if (parts.length >= 2) jsonObject = (Object) parts[1];
                break;
            case "optional"		:
                if (values.get(parts[1]) != null) {
                    jsonObject = (Object) values.get(parts[1]);
                }
                else
                    jsonObject = (Object) (new String(""));
                break;
            case "getOrDefault"		:
                if (values.containsKey(parts[1])){
                    jsonObject = (Object) values.get(parts[1]);
                }
                else {
                    if (parts.length == 3) {
                        jsonObject = parts[2];
                    }
                    else{
                        jsonObject = "";
                    }
                }
                break;
            case "append"		:
                if (values.containsKey(parts[2]))
                    jsonObject = (Object) parts[1] + values.get(parts[2]).toString();
                else if (parts.length == 4)
                    jsonObject = (Object) parts[1] + parts[3];
                else
                    jsonObject = (Object) parts[1];
                break;
            case "load"	:
                String filename = parts[1];
                Object object = JsonUtils.getObjectFromFile(filename);
                jsonObject = getJSONObjectAsObject(object, values);
                break;
            default				:
                System.out.println("Bad configuration for this");
                break;
        }

        return jsonObject;
    }
    /**
     * This methods cast the class of object v and call the proper method to get its value.
     * @param v object to be transformed from the template to a real json value.
     * @param values Map with the values to be used to fill out the object v.
     * @return An object representing v after transformation.
     *
     */
    public static Object getJSONObjectAsObject(Object v, Map<String, Object> values) {
        Object jsonObject = null;
        if (v instanceof Map) {
            jsonObject = (Object) getJsonMap((Map<String, Object>) v, values);
        }
        else if (v instanceof ArrayList){
            jsonObject = (Object) getJsonArray((ArrayList<Object>) v, values);
        }
        else if (v instanceof String){
            jsonObject = getObjectValue((String) v, values);
        }
        else {
            jsonObject = (Object) (new String("Error in configuration model"));
        }
        return jsonObject;
    }
}
