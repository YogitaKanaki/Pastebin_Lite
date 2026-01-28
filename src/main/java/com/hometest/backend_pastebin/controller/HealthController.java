package com.hometest.backend_pastebin.controller;

import com.hometest.backend_pastebin.repo.PasteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    PasteRepo repo;

    @GetMapping("/api/healthz")
    public Map<String, Boolean> health() {
        repo.count();
        return Map.of("ok", true);
    }
}

