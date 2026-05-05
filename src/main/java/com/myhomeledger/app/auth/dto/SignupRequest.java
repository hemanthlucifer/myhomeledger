package com.myhomeledger.app.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 10, max = 10)
    private String phoneNumber;

    @NotBlank
    @Size(min = 8, max = 128)
    private String password;
}
