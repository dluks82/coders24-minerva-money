package dev.dluks.minervamoney.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/docs")
public class DocsController {

    @GetMapping
    public ResponseEntity<Void> getSwaggerDocs() throws URISyntaxException {
        String redirectUrl = "/swagger-ui/index.html";
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new java.net.URI(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}