package com.hometest.backend_pastebin.repo;

import com.hometest.backend_pastebin.model.Paste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasteRepo extends JpaRepository<Paste, String> {
}

