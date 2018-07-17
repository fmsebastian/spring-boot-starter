package com.poc.initializer.web.controllers;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RequestMapping("/xml")
@RestController
public class XmlController {

    @RequestMapping(value = "/message/string",
            method = RequestMethod.POST,
            consumes = "application/xml"
    )
    public String receiveStringMessage(@RequestBody String body){
        return "received body: " + body;
    }

    @RequestMapping(value = "/message/xml",
            method = RequestMethod.POST,
            consumes = "application/xml",
            produces = "application/json"
    )
    public String receivedXmlToJsonMap(@RequestBody String body){
        String result = "No exceptions";
        JSONObject jsonObj = XML.toJSONObject(body, true);
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("RequestXml", body);
        request.put("result", result);
        request.putAll(jsonObj.toMap());
        JSONObject jsonMap = new JSONObject(request);

        return jsonMap.toString();
    }

    @RequestMapping(value = "/message/xml/asJson",
            method = RequestMethod.POST,
            consumes = "application/xml",
            produces = "application/json"
    )
    public String receivedXmlToJsonMapWithTypeValues(@RequestBody String body){
        String result = "No exceptions";
        JSONObject jsonObj = XML.toJSONObject(body);
        Map<String, Object> request = new LinkedHashMap<String, Object>();
        request.put("RequestXml", body);
        request.put("result", result);
        request.putAll(jsonObj.toMap());
        JSONObject jsonMap = new JSONObject(request);
        //return "received body: \n" + body.toString() + result + jsonObj.toString();
        return jsonMap.toString();
    }
}