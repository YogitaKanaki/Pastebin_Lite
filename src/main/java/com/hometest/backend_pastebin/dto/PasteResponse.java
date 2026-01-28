package com.hometest.backend_pastebin.dto;


import java.time.Instant;

public record PasteResponse(
        String content,
        Integer remaining_views,
        Instant expires_at
) {
}

