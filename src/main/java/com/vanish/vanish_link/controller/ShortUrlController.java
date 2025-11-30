package com.vanish.vanish_link.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vanish.vanish_link.dto.CreateRequest;
import com.vanish.vanish_link.dto.CreateResponse;
import com.vanish.vanish_link.service.ShortUrlService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShortUrlController {
    private final ShortUrlService shortUrlService;
    
    @PostMapping("/shorten")
    public ResponseEntity<CreateResponse> shorten(@Valid @RequestBody CreateRequest request){
        CreateResponse response = shortUrlService.create(request);
        return ResponseEntity.ok(response);
    }
}
