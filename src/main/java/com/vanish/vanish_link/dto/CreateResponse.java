package com.vanish.vanish_link.dto;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class CreateResponse {
    String shortUrl;
    String message;
    LocalDateTime expiresAt;
}
