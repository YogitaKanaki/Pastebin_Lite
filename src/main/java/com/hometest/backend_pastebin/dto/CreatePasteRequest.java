package com.hometest.backend_pastebin.dto;



public record CreatePasteRequest(
        String content,
        Integer ttl_seconds,
        Integer max_views
) {
}

