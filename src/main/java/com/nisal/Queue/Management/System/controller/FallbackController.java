package com.nisal.Queue.Management.System.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @RequestMapping("/**")
    public ResponseEntity<?> fallback() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("URL not found");
    }
}
