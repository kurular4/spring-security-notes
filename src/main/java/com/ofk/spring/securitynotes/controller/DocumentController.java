package com.ofk.spring.securitynotes.controller;

import com.ofk.spring.securitynotes.model.Document;
import com.ofk.spring.securitynotes.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("document")
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    //@PreAuthorize("hasAnyAuthority('WRITE', 'READ')")
    public ResponseEntity<Document> getDocument(@PathVariable String id) {
        return new ResponseEntity<>(documentService.getDocument(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    //@PreAuthorize("hasAnyAuthority('WRITE', 'READ')")
    public ResponseEntity<List<Document>> getDocument() {
        return new ResponseEntity<>(documentService.getAllDocuments(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    //@PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity<Boolean> deleteDocument(@PathVariable String id) {
        return new ResponseEntity<>(documentService.deleteDocument(id), HttpStatus.OK);
    }
}
