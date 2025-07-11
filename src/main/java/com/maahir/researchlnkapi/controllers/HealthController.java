package com.maahir.researchlnkapi.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String index() {
        return "API is running";
    }

    @GetMapping("/health")
    public String health(){
        return "health check is OK";
    }
}
