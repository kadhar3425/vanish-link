package com.vanish.vanish_link.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Service;

import com.vanish.vanish_link.dto.CreateRequest;
import com.vanish.vanish_link.dto.CreateResponse;
import com.vanish.vanish_link.entity.ShortUrl;
import com.vanish.vanish_link.repository.ShortUrlRepository;


import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public CreateResponse create(CreateRequest request){

        String shortCode = generateUniqueCode();
        LocalDateTime expiresAt = request.getExpiryMinutes()==null?
        LocalDateTime.now().plusDays(7):LocalDateTime.now().plusMinutes(request.getExpiryMinutes());

        ShortUrl entity = ShortUrl.builder()
            .originalUrl(request.getUrl())
            .shortCode(shortCode)
            .expiresAt(expiresAt)
            .oneTimeView(request.isOneTime())
            .passwordHash(request.getPassword()!=null?passwordEncoder.encode(request.getPassword()):null)
            .build();
        shortUrlRepository.save(entity);
        // Redis TTL for auto expiry
        long seconds = Duration.between(LocalDateTime.now(), expiresAt).getSeconds();
        redisTemplate.opsForValue().set("url:"+shortCode,"active",seconds, TimeUnit.SECONDS);

        String shortUrl = "https://vanish.rj/"+shortCode;
        String msg = request.isOneTime()?"Vanishes after first view!":"Expires in "+request.getExpiryMinutes()+" minutes.";

        return new CreateResponse(shortUrl, msg, expiresAt);
    }
    private String generateUniqueCode(){
        String code;
        do {
            code = RandomStringUtils.random(7,BASE62);
        } while (shortUrlRepository.existsByShortCode(code));
        return code;
    }
}
