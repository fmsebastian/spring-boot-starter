package com.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * This class is used to transform a normal Json into a flatten one. Internal maps will be transformed appending the subsequent keys separated by dots ('.').
 * Arrays will be left as such, but internal maps will be transformed too. Normal key-value pairs will be left as such.
 * <p>
 * <pre>
 * Example:					will be transformed to:
 * "key":"value"					"key":"value
 * "map":{					"map.k1.k11":"v1",
 * 	"k1":{					"map.k1.k12":"v2",
 * 		"k11":"v1",			"array":[
 * 		"k12":"v2"			"a1",
 * 	}					"a2",
 * "array":[					{
 * 	"a1",					"ka1":"va1",
 * 	"a2",					"ka2":"va2",
 * 	{					"kmap.km1":"vm1"
 * 	"ka1":"va1",				}
 * 	"ka2":"va2"				]
 * 	"kmap":{
 * 		"km1":"vm1"
 * 		}
 * 	}
 * ]
 *</pre>
 */
@UtilityClass
public class MapFlattener {

    /*private MapFlattener() {
        throw new IllegalStateException("This class should not be instantiated");
    }*/

    public static List<Entry<String, Object>> simplifyMapToEntryList(Map<String, Object> map) throws InstantiationException, IllegalAccessException {
        Map<String, Object> simplifiedMap = simplifyMap(map);
        return new ArrayList<>(simplifiedMap.entrySet());
    }

    /**
     * This methods simplify the Object selecting between the diferent instances (String, Map, ArrayList).
     * <br> It returns a <b>new object</b>, without modifying the input <b>value</b> (No <b>Side Effects</b>)
     * @param value	to be simplified
     * @return the simplified value. A 'new String(value)' if it is a String. Null if value is not one of the above instances or value is null.
     * @see #simplifyMap(Map)
     * @see #simplifyList(List)
     */
    public static Object simplifyObject(Object value) throws InstantiationException, IllegalAccessException {
        if (value == null)
            return null;
        else if (value instanceof Map)
            return simplifyMap((Map<String, Object>) value);
        else if (value instanceof List)
            return simplifyList((List<Object>) value);
        else if (value instanceof String)
            return new String((String) value);
        else
            System.out.println("Class not expected for object: " + value.toString() + ": " + value.getClass());
        return null;
    }
    /**
     * This method flatten the input map, returning a new one in the process (no side effects).
     * <br>Simple key-values entries will be left as such, ArrayList will be internally simplified using ({@link #simplifyList(List)}.
     * <br>For key-map entries, the map will be simplified and the key will be prepended to all elements in the map using {@link #prependNameToMapKeys(String, Map)}.
     * <br>For other instances of values, that key-value will be discarded.
     * @param map to be flattened.
     * @return	A new map with values of the input one flattened.
     */
    public static Map<String, Object> simplifyMap(Map<String, Object> map) throws IllegalAccessException, InstantiationException {
        Map<String, Object> newMap = (Map<String, Object>) (map.getClass()).newInstance();
        for (Entry<String, Object> entrySet : map.entrySet()) {
            String k = entrySet.getKey();
            Object v = entrySet.getValue();
                if (v == null) {
                    newMap.put(k, null);
                } else if (v instanceof Map) {
                    Map<String, Object> valuesMap = simplifyMap((Map) v);
                    newMap.putAll(prependNameToMapKeys(k, valuesMap));
                } else if (v instanceof List) {
                    List<Object> list = simplifyList((List) v);
                    newMap.put(k, list);
                } else if (v instanceof String) {
                    newMap.put(k, new String((String) v));
                }
                else if (v instanceof Number) {
                    newMap.put(k, v);
                }
                else if (v instanceof Boolean) {
                    newMap.put(k,v);
                }
                else {
                    System.out.println("Class not expected for map: " + v.toString() + ": " + v.getClass());
                }
        }
        return newMap;
    }
    /**
     * Prepend <b>str</b> param to all keys in <b>map</b>. Return a new map
     * @param name the String to be prepended to map keys.
     * @param map	to which prepend str.
     * @return	A new map with str prepended to each key and same values as map.
     */
    private static Map<String, Object> prependNameToMapKeys(String name, Map<String, Object> map) throws IllegalAccessException, InstantiationException {
        Map<String, Object> newMap = (Map<String, Object>) map.getClass().newInstance();
        map.forEach((k,v) -> {
            newMap.put(name +"."+k, v);
        });
        return newMap;
    }
    /**
     * This simplifies every object of the ArrayList using {@link #simplifyObject(Object)} method. Returns a new ArrayList.
     * @param list to be simplified
     * @return	A new array (No side effects).
     * @see #simplifyObject(Object)
     */
    private static List<Object> simplifyList(List<Object> list) throws IllegalAccessException, InstantiationException {
        List<Object> newList = (List<Object>) list.getClass().newInstance();
        for (Object obj : list) {
                Object simplified = simplifyObject(obj);
                newList.add(simplified);
        }
        return newList;
    }

    private static Map.Entry<String, Object> createEntry(String key, Object value) {
        return new MapEntryImplementation(key, value);
    }

    private class MapEntryImplementation implements Map.Entry<String, Object> {
        private String key;
        private Object value;

        MapEntryImplementation(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            this.value = value;
            return value;
        }
    }
}
