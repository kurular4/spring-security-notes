package com.ofk.spring.securitynotes.service;

import com.ofk.spring.securitynotes.model.Document;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple service, no actual database as it is not primary concern
 */
@Service
public class DocumentService {
    private final List<Document> titles = new LinkedList<>(Arrays.asList(
            new Document("a1", "Animal Rescue Report", "No content, yet"),
            new Document("a2", "Oceans sea level report", "No content, yet"),
            new Document("a3", "Possible effects of global warming on rain forests", "No content, yet")
    ));

    public Document getDocument(String id) {
        return titles
                .stream()
                .filter(document -> document.getId().equals(id))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public List<Document> getAllDocuments() {
        return titles;
    }

    public boolean deleteDocument(String id) {
        return titles.remove(titles
                .stream()
                .filter(document -> document.getId().equals(id))
                .findFirst()
                .orElseThrow(RuntimeException::new));
    }
}
