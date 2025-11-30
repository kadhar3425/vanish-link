package com.vanish.vanish_link.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.vanish.vanish_link.entity.ShortUrl;
import com.vanish.vanish_link.repository.ShortUrlRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RedirectController {
    private final ShortUrlRepository shortUrlRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/{code}")
    public ResponseEntity<?> redirect(@PathVariable String code, @RequestParam(required = false) String p) {

        String redisKey = "url:" + code;

        // 1.Redis expiry check

        if (!Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            return ResponseEntity.status(404).body(Map.of("error", "Link expired or does not exist."));
        }

        ShortUrl shortUrl = shortUrlRepository.findByShortCode(code).orElse(null);
        if (shortUrl == null || shortUrl.isOneTimeView() || LocalDateTime.now().isAfter(shortUrl.getExpiresAt())) {
            redisTemplate.delete(redisKey);
            return ResponseEntity.status(404).body(Map.of("error", "Link expired or does not exist."));
        }
        // 2.Password protection check

        if (shortUrl.getPasswordHash() != null) {
            if (p == null || !passwordEncoder.matches(p, shortUrl.getPasswordHash())) {
                return ResponseEntity.status(401).body(Map.of("error", "Password required or incorrect password."));
            }
        }

        // 3. One-time view + click count

        if (shortUrl.isOneTimeView()) {
            shortUrl.setOneTimeView(true);
            redisTemplate.delete(redisKey);
        }

        // 4.Final redirect

        return ResponseEntity.status(302).header(HttpHeaders.LOCATION, shortUrl.getOriginalUrl()).build();
    }
}
