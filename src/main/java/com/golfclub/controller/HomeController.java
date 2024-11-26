package com.golfclub.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "running");
        response.put("message", "Golf Club API is running");
        response.put("api_docs", "Available endpoints: /api/v1/members/, /api/v1/tournaments/");
        return response;
    }
}