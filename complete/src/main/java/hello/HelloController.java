package hello;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import testClasses.DerivedObjectChecks;

@RestController
public class HelloController {
    
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/test")
    public String testsCalls() {
        boolean derivedObjectcheck = DerivedObjectChecks.checkCompareDerivedObjects();
        return "test done: " + derivedObjectcheck;
    }
}
