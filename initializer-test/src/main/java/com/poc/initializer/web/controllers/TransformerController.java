package com.poc.initializer.web.controllers;

import com.poc.initializer.web.services.*;
import com.model.JsonPair;
import com.utils.MapFlattener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TransformerController {

    @Autowired
    private JsonComparisonService jsonComparisonService;

    @RequestMapping(value = "/json",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public JsonPair compareJsonObjects(@RequestBody Map<String, Object> bodyMap) throws InstantiationException, IllegalAccessException {
        JsonPair body = JsonPair.builder()
                .original((Map<String, Object>) bodyMap.get("original"))
                .produced((Map<String, Object>) bodyMap.get("produced"))
                .build();
        Map<String, Object> original = body.getOriginal();
        Map<String, Object> produced = body.getProduced();
        Map<String, Object> originalFlattened = MapFlattener.simplifyMap(original);
        Map<String, Object> producedFlattened = MapFlattener.simplifyMap(produced);
        List<Map.Entry<String, Object>> originalFlattenedAsList = MapFlattener.simplifyMapToEntryList(original);
        List<String> errors = jsonComparisonService.compareJsonMapValues(originalFlattened, producedFlattened);
        List<String> errorsFromList = jsonComparisonService.compareJsonMapValues(originalFlattenedAsList, producedFlattened);
        return body.withErrors(errors).withResult(200);
        //return body;
    }

    @RequestMapping(value = "/jsonPair",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public JsonPair compareJsonPair(@RequestBody JsonPair body) throws InstantiationException, IllegalAccessException {
        Map<String, Object> original = body.getOriginal();
        Map<String, Object> produced = body.getProduced();
        Map<String, Object> originalFlattened = MapFlattener.simplifyMap(original);
        Map<String, Object> producedFlattened = MapFlattener.simplifyMap(produced);
        List<String> errors = jsonComparisonService.compareJsonMapValues(originalFlattened, producedFlattened);

        return body.withErrors(errors).withResult(200);
        //return body;
    }
}
