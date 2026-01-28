package com.hometest.backend_pastebin.controller;

import com.hometest.backend_pastebin.dto.CreatePasteRequest;
import com.hometest.backend_pastebin.dto.CreatePasteResponse;
import com.hometest.backend_pastebin.dto.PasteResponse;
import com.hometest.backend_pastebin.model.Paste;
import com.hometest.backend_pastebin.repo.PasteRepo;
import com.hometest.backend_pastebin.service.PasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PasteController {

    @Autowired
    private PasteService service;

    @PostMapping("/pastes")
    public ResponseEntity<?> create(@RequestBody CreatePasteRequest req) {
        if (req.content() == null || req.content().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "content required"));
        }

        Paste p = service.create(req.content(), req.ttl_seconds(), req.max_views());

        return ResponseEntity.ok(new CreatePasteResponse(
                p.getId(),
                "/api/pastes/" + p.getId()
        ));
    }

    @GetMapping("/pastes/check/{id}")
    public ResponseEntity<?> check(@PathVariable String id) {
        try {
            Instant now = Instant.now();

            Paste p = service.getPaste(id);       // ✅ correct
            boolean canView = service.canView(id, now);

            Integer remaining = p.getMaxViews() == null
                    ? null
                    : Math.max(0, p.getMaxViews() - p.getViews());

            return ResponseEntity.ok(Map.of(
                    "canView", canView,
                    "views", p.getViews(),
                    "maxViews", p.getMaxViews(),
                    "remainingViews", remaining,
                    "expiresAt", p.getExpiresAt()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "not found"));
        }
    }


    @GetMapping("/pastes/{id}")
    public ResponseEntity<?> fetch(
            @PathVariable String id,
            @RequestHeader(value = "x-test-now-ms", required = false) Long testNow
    ) {
        try {
            Instant now = testNow != null
                    ? Instant.ofEpochMilli(testNow)
                    : Instant.now();

            Paste p = service.fetch(id, now);

            Integer remaining = p.getMaxViews() == null
                    ? null
                    : Math.max(0, p.getMaxViews() - p.getViews());

            return ResponseEntity.ok(new PasteResponse(
                    p.getContent(),
                    remaining,
                    p.getExpiresAt()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", "not found"));
        }
    }
}
