package com.myhomeledger.app.user.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetUserDTO {

    private String username;
    private String phoneNumber;
}
