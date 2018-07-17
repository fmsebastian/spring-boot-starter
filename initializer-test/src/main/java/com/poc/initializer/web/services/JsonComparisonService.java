package com.poc.initializer.web.services;

import com.model.ComparisonMethodsEnum;
import com.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.model.ComparisonMethodsEnum.*;

@Service
@Slf4j
public class JsonComparisonService  {
    
    private final ArrayList<ComparisonMethodsEnum> comparisonFilters;
    private final List<String> fieldExclusioniList;
    private final Map<String, String> movedFields;

    public JsonComparisonService () {
        comparisonFilters = new ArrayList<ComparisonMethodsEnum>(Arrays.asList(SKIPFIELDSINEXCLUSIONLIST, COMPAREBYSTRUCTURE, COMPAREMOVEDFIELDS));
        fieldExclusioniList = new ArrayList<String>(Arrays.asList("whatever"));
        movedFields = new HashMap<String, String>();
    }

    public List<String> compareJsonMapValues(Map<String, Object> one, Map<String, Object> two) {
        List<String> results = new ArrayList<>();
        if (areCollectionsBothNullOrEmpty((Collection) one, (Collection) two, results)) {
            return results;
        }
        results.addAll(
                one.entrySet().stream()
                        .filter(entry -> !matchObjectInJson(entry, two, true))
                        .map(entry -> "field " + entry.getKey() + " is not found in second json")
                        .collect(Collectors.toList()));
        results.addAll(
                (two.entrySet()).stream()
                        .filter(entry -> !matchObjectInJson(entry, one, false))
                        .map(entry -> "field " + entry.getKey() + " is not found in first json")
                        .collect(Collectors.toList()));
/*        for (Map.Entry<String, Object> entry : one.entrySet()) {
            if (!matchObjectInJson(entry, two, true)) {
                results.add("Field " + entry.getKey() + " with value " + entry.getValue() + " does not match in second json");
            }
        }*/
        return results;
    }

    private boolean matchObjectInJson(Map.Entry<String, Object> entry, Map<String, Object> json, boolean deleteIfFound) {
        // TODO: Change the hole process of selecting method by the use of ENUMS (instead of string list for comparisonFilters)
        if (entry == null) {
            return false;
        }
        for (ComparisonMethodsEnum method : comparisonFilters) {
            switch (method) {
                case COMPAREBYSTRUCTURE:
                    if (compareByStructure(entry, json, deleteIfFound)){
                        return true;
                    }
                    break;
                case COMPAREMOVEDFIELDS:
                    if (compareMovedFields(entry, json, deleteIfFound)) {
                        return true;
                    }
                    break;
                case SKIPFIELDSINEXCLUSIONLIST:
                    if (skipFieldsInExclusionList(entry, json, deleteIfFound)) {
                        return true;
                    }
                    break;
                default:
                    log.error("No " + method + " method found.");
                    break;
            }
        }
        return false;
    }

    public boolean skipFieldsInExclusionList(Map.Entry<String, Object> entry, Map<String, Object> json, boolean deleteIfFound) {
        return fieldExclusioniList.contains(entry.getKey());
    }

    public boolean compareByStructure(Map.Entry<String, Object> entry, Map<String, Object> json, boolean deleteIfFound) {
        return JsonUtils.checkValueInMap(entry, json, deleteIfFound);
    }

    public boolean compareMovedFields(Map.Entry<String, Object> entry, Map<String, Object> json, boolean deleteIfFound) {
        String movedEntryKey = movedFields.get(entry.getKey());
        return (movedEntryKey != null) &&
            JsonUtils.checkValueInMap(new AbstractMap.SimpleEntry(movedEntryKey, entry.getValue()), json, deleteIfFound);
    }

    private boolean areCollectionsBothNullOrEmpty(Collection one, Collection two, List<String> results) {
        if (one == null && two == null) {
            return true;
        }
        if (one == null || two == null || (one.isEmpty() != two.isEmpty())) {
            results.add("Only one of the json structures is either null or empty");
            return true;
        }
        return false;
    }

    private List<String> areCollectionsBothNullOrEmpty(Collection one, Collection two) {
        if (one == null && two == null) {
            return Collections.emptyList();
        }
        if (one == null || two == null || (one.isEmpty() != two.isEmpty())) {
            return Collections.singletonList("Only one of the json structures is either null or empty");
        }
        return null;
    }
}
