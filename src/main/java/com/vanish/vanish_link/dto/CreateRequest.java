package com.vanish.vanish_link.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRequest {
    @NotBlank(message = "url is required")
    String url;
    Integer expiryMinutes;
    String password;
    boolean oneTime;
}
