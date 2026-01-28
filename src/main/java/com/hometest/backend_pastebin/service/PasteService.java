package com.hometest.backend_pastebin.service;

import com.hometest.backend_pastebin.model.Paste;
import com.hometest.backend_pastebin.repo.PasteRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class PasteService {

    @Autowired
    public PasteRepo repo;

    public boolean canView(String id, Instant now) {
        Paste p = repo.findById(id).orElseThrow();
        return !p.isExpired(now) && !p.isViewLimitExceeded();
    }

    public Paste getPaste(String id) {
        return repo.findById(id).orElseThrow();
    }

    public Paste create(String content, Integer ttl, Integer maxViews) {
        Paste p = new Paste();
        p.setId(UUID.randomUUID().toString());
        p.setContent(content);
        p.setViews(0);
        p.setMaxViews(maxViews);

        if (ttl != null) {
            p.setExpiresAt(Instant.now().plusSeconds(ttl));
        }

        return repo.save(p);
    }



    public Paste fetch(String id, Instant now) {
        Paste p = repo.findById(id).orElseThrow();

        // Only allow fetch if it’s viewable
        if (!canView(id, now)) {
            throw new NoSuchElementException();
        }

        // Increment views exactly once
        p.setViews(p.getViews() + 1);

        return p;
    }
}

