package com.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.json.simple.*;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * This class store methods to load JSON/Map from files, or to transform JSON Objects from/to other formats (JSONObject to Map, String to Map...)
 * <br>All methods are static and free of side effects, since the returned Object is always new.
 * @author fsebastian
 *
 */
public class JsonUtils {
    private JsonUtils(){
        throw new IllegalStateException("You should not pass!! (Nor implement this class");
    }

    /**
     * Parse a file using {@link org.json.simple.parser.JSONParser} of <b>org.json.simple</b> library.
     * <br>It is essentially the same method as {@link #getObjectFromFile(String)}, casting its result to JSONObject.
     * @param filename of the file to parser (full path)
     * @return {@link org.json.simple.JSONObject} representing the file, null if any exception caught.
     */
    public static JSONObject getJsonObjectFromFile (String filename) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        jsonObject = (JSONObject) getObjectFromFile(filename);
        return jsonObject;
    }
    /**
     * Parse a file using {@link org.json.simple.parser.JSONParser} of <b>org.json.simple</b> library.
     * @param filename of the file to parser (full path)
     * @return An object representing the file, null if any exception caught.
     * 			The object is an instantiation of {@link org.json.simple.JSONObject}
     */
    public static Object getObjectFromFile (String filename) {
        JSONParser parser = new JSONParser();
        Object object = null;
        try {
            object = parser.parse(new FileReader(filename));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object;
    }
    /**
     * This reads a json file and convert it to a LinkedHashMap object representing it.
     * <br>It uses {@link #getJsonObjectFromFile(String)} to read the file and {@link #getLinkedMapFromJSONObject(JSONObject)} to convert it.
     * @param filename of the file to parser (full path)
     * @return A {@link java.util.LinkedHashMap} representing the file, null if any exception caught.
     * @throws ParseException if {@link #getLinkedMapFromJSONObject(JSONObject)} throws it.
     * @see #getJsonObjectFromFile(String)
     * @see #getLinkedMapFromJSONObject(JSONObject)
     */
    public static LinkedHashMap<String, Object> getLinkedMapFromFile (String filename) throws ParseException{
        JSONObject jsonObject = getJsonObjectFromFile(filename);
        LinkedHashMap<String, Object> map = getLinkedMapFromJSONObject(jsonObject);
        return map;
    }
    /**
     * This method changes the internal classes of the JSONObject object, using LinkedHashMap for json's map and ArrayList for json's arrays.
     * @param json object to be converted.
     * @return A {@link java.util.LinkedHashMap} representing the same object as the initial json.
     * @throws ParseException if JSONObject can not be converted.
     * @see org.json.simple.parser.ContainerFactory
     */
    public static LinkedHashMap<String, Object> getLinkedMapFromJSONObject (JSONObject json) throws ParseException{
        JSONParser parser = new JSONParser();
        ContainerFactory orderedKeyFactory = new ContainerFactory()
        {
            public ArrayList creatArrayContainer() {
                return new ArrayList();
            }
            public Map createObjectContainer() {
                return new LinkedHashMap();
            }
        };
        Object obj = parser.parse(json.toJSONString(),orderedKeyFactory);
        LinkedHashMap map = (LinkedHashMap)obj;
        return map;
    }
    /**
     * This converts a String representing a flat json (a map of key-values, where values are strings) to a Map.
     * <br>The string must be a exact representation of a json, including the use of <b>"</b> for it to work.
     * @param str String to be parsed.
     * @return A Map representing the String.
     */
    public static Map<String, Object> stringToFlatJSON(String str){
        // Example str:
        // {"Errors":"OK","external_ticket_id":"RUBT-79075","status":200}
        // For this str it will not work (but it will return a wrong Map {"key":"value", "map":{"k1":"v1","k2":"v2"},"key2":"value2"}
        String[] fields = str.split(",");
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        for(int i = 0; i<fields.length; i++){
            String[] parts = fields[i].split(":");
            String key, value;
            if (parts.length==2){
                int beginIndex = parts[0].indexOf("\"");
                int endIndex = parts[0].lastIndexOf("\"");
                if (beginIndex != endIndex)
                    key = parts[0].substring(beginIndex+1, endIndex);
                else
                    key = parts[0];

                beginIndex = parts[1].indexOf("\"");
                endIndex = parts[1].lastIndexOf("\"");
                if (beginIndex != endIndex)
                    value = parts[1].substring(beginIndex+1, endIndex);
                else
                    value = parts[1];
                map.put(key, value);
            }
        }
        return map;
    }

    /*********************************
     **                             **
     **     COMPARISON METHODS      **
     **                             **
     ********************************/

    private static Object lookForValueInMap(Map.Entry<String, Object> entry, Map<String, Object> map) {
        return map.get(entry.getKey());
    }

    private static Object lookForElementInList(Object element, List<Object> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Object first = list.get(0);
        if (element instanceof String && first instanceof String) {
            return list.contains(element);
        }
        if (element instanceof Map && first instanceof Map) {
            for (Object list2elem : list) {
                if (compareJsonMaps((Map<String, Object>) element, (Map<String, Object>) list2elem)) {
                    return true;
                }
            }
        }
        return null;
    }

    private static boolean compareJsonMaps(Map<String, Object> map1, Map<String, Object> map2) {
        if (map2 == null || map1 == null) {
            return Objects.equals(map1, map2);
        }
        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            if (!checkValueInMap(entry, map2, false)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkValueInMap(Map.Entry<String, Object> entry, Map<String, Object> map, boolean deleteIfFound) {
        Object value2 = lookForValueInMap(entry, map);
        if (compareJsonObject(entry.getValue(), value2)) {
            if (deleteIfFound) {
                map.remove(entry.getKey());
            }
            return true;
        }
        return false;
    }

    private static boolean compareJsonObject(Object value1, Object value2) {
        if (value1 == null || value2 == null) {
            return Objects.equals(value1, value2);
        }
        if (value1 instanceof String && value2 instanceof String) {
            return ((String) value1).equalsIgnoreCase((String) value2);
        }
        if (value1 instanceof List && value2 instanceof List) {
            return compareJsonLists((List) value1, (List) value2);
        }
        if (value1 instanceof Map && value2 instanceof Map) {
            return compareJsonMaps((Map) value1, (Map) value2);
        }
        return false;
    }

    private static boolean compareJsonLists(List list1, List list2) {
        if (list1 == null || list2 == null) {
            return Objects.equals(list1, list2);
        }

        if (list1.size() != list2.size()) {
            return false;
        }

        for(Object list1Elem : list1) {
            Object list2Elem = lookForElementInList(list1Elem, list2);
            if (!compareJsonObject(list1Elem, list2Elem)) {
                return false;
            }
        }
        return true;
    }
}