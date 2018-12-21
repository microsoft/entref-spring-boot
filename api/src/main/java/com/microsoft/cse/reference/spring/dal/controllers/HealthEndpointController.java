package com.microsoft.cse.reference.spring.dal.controllers;

import org.springframework.web.bind.annotation.*;

/**
 * Create a custom endpoint that returns status OK when Java App is running
 */
@RestController
public class HealthEndpointController {

    @RequestMapping(method = RequestMethod.GET, value = "/health")
    public String getStatus() {
        return new String("Alive");
    }
}
