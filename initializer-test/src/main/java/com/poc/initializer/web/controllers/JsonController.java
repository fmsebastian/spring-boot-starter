package com.poc.initializer.web.controllers;

import com.utils.MapFlattener;
import org.json.XML;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.json.simple.parser.*;

import java.util.Map;

@RequestMapping("/json")
@RestController
public class JsonController {

    @RequestMapping(value = "/message/string",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public String receiveStringMessage(@RequestBody String body){
        return "received body: " + body;
    }

    @RequestMapping(value = "/message/map",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public String receiveJsonMap(@RequestBody Map<String, Object> body){
        return "received body: " + body.toString();
    }

    @RequestMapping(value = "/message/map/flatten",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json"
    )
    public String flattenReceivedJsonMapAddOriginal(@RequestBody Map<String, Object> body){
        String result = "No exceptions";
        Map<String, Object> flattenMap;
        try {
            flattenMap = flattenReceivedJsonMap(body);
            result = "\nNo exceptions. \n" + flattenMap.toString();
        } catch (InstantiationException|IllegalAccessException ex) {
            ex.printStackTrace();
            result = "\n Imposible to simplify input map: " + ex.toString();
        }
        return "Received body: " + body.toString() + result;
    }

    @RequestMapping(value = "/message/map/flatten/onlyFlat",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json"
    )
    public Map<String, Object> flattenReceivedJsonMap(@RequestBody Map<String, Object> body) throws IllegalAccessException, InstantiationException {
        Map<String, Object> flattenMap;
        try {
            flattenMap = MapFlattener.simplifyMap(body);
        } catch (InstantiationException|IllegalAccessException ex) {
            ex.printStackTrace();
            flattenMap = body.getClass().newInstance();
            flattenMap.put("Response", "500");
            flattenMap.put("Request payload: ", body);
            flattenMap.put("Exception: ", ex);
        }
        return flattenMap;
    }

    @RequestMapping(value = "/message/JSONObject",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public String receiveJson(@RequestBody String body){
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(body);
        } catch (ParseException e) {
            jsonObject = null;
            e.printStackTrace();
        }
        return "received body: \n" + body + "\n jsonObject: " + jsonObject;
    }

    @RequestMapping(value = "/message/Json/asXML",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/xml"
    )
    public String transformJSONtoXML(@RequestBody Map<String, Object> body){
        JSONObject job = new JSONObject();
        job.putAll(body);
        //String jsonString = JSONObject.toJSONString(body);
        //JSONObject jsonMap  = new JSONObject();
        //jsonMap.putAll(body);
        String xml = XML.toString(job);
        return xml;
    }
    /*public String transformJSONtoXML(@RequestBody String body){
        String xmlString = XML.toString(body);
        return xmlString;
    }*/
}
