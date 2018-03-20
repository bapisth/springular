package com.hemendra.springular.resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hemendra
 */
@RestController
@RequestMapping("/say")
public class HelloWorld {
    @GetMapping(value = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
    private String sayHello() {
        return "Hello from Spring Boot";
    }

    @GetMapping("/hi")
    private String sayHi() {
        return "Hi From Spring";
    }

    @GetMapping("/kadali")
    private String test() {
        return "Kadali";
    }
}
