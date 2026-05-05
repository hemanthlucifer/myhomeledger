package com.myhomeledger.app.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @NotNull(message = "Username cannot be null")
    private String username;

    @NotNull(message = "Phone number cannot be null")
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
    private String phoneNumber;
}
