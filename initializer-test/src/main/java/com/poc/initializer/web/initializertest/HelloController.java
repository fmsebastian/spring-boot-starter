package com.poc.initializer.web.initializertest;

import com.poc.initializer.web.data.LombokDataAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.val;

@Controller
public class HelloController {

    @RequestMapping("/")
    public String hello() {
        System.out.println("hello - started");
        return "Initializer started";
    }

    @RequestMapping("lombok")
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
}
