package com.poc.initializer.web.controllers;

import com.model.BaseRecord;
import com.model.RecordExample;
import com.model.RecordExampleV2;
import com.poc.initializer.web.data.LombokDataAnnotation;
import org.springframework.web.bind.annotation.*;
import lombok.val;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class HelloController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/test/{id}", method = RequestMethod.GET)
    public String test(@PathVariable("id") String id) {
        return "testing id " + id + " formatting: " + String.format("Testing ${id}", id);
    }

    @RequestMapping(value = "/test/{id}/params", method = RequestMethod.GET)
    public String param(@RequestParam("name") String name, @PathVariable("id") String id, @RequestParam(value = "orderBy", required = false) String orderBy) {
        String result = "name: " + name +  ", order by " + orderBy + " and id: " + id;
        return "Result: " + result;
    }

    @RequestMapping("/lombok")
    public String lombok() {
        //return new LombokDataAnnotation
        LombokDataAnnotation.LombokDataAnnotationBuilder lombokBuilder =  LombokDataAnnotation.builder();
        System.out.println("created builder: " + lombokBuilder.toString());
        System.out.println("plus name n1 - " + lombokBuilder.name("n1").toString());
        System.out.println("plus id 123 - " + lombokBuilder.id("123").toString());
        System.out.println("plus crCard 789 - " + lombokBuilder.creditCard(789).toString());
        System.out.println("build: - " + lombokBuilder.build());


        try {
            LombokDataAnnotation lombokNull = LombokDataAnnotation.builder().build();
            System.out.println("lombok null? " + lombokNull.toString());
        }
        catch (Exception e ) {
            System.out.println("Lombok null does not work");
        }


        val lombokBuilded = LombokDataAnnotation.builder().name("pepe").id("456").creditCard(6654).build();
        System.out.println("lombok builded: " + lombokBuilded.toString());
        /*LombokDataAnnotation l2 = new LombokDataAnnotation("fran2", "0123");*/
        //LombokDataAnnotation lombok = new LombokDataAnnotation("fran", "1234").setCreditCard(789456132);
        return "test lombok";
    }



    @RequestMapping(value = "/test/generateClass",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public String generateClass(@RequestBody Map<String, Object> body){

        /**
         * RecordExample:
            valuation: calculationInput.additionalData.partPriceFactor
            loaded: calculationInput.additionalData.roundOffCode
         */

        List<BaseRecord> recordList = new ArrayList<>();
        RecordExample recordExample = new RecordExample();
        RecordExampleV2 recordExampleV2 = new RecordExampleV2();

        recordList.add(recordExample);
        recordList.add(recordExampleV2);

        for (BaseRecord record : recordList) {
            if (record instanceof RecordExample) {
                RecordExample example = (RecordExample) record;
                for (Field field : RecordExample.class.getFields()) {
                    System.out.println("field: " + field.getName() + " - " + field.toString());

                }
            }
            else if (record instanceof RecordExampleV2) {

            }
            else {
                System.out.println("Error!! class unreckognized");
            }
        }

        return "ok";
    }
}
