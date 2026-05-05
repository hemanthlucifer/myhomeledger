package com.myhomeledger.app.user.service;

import com.myhomeledger.app.user.dto.GetUserDTO;

public interface GetUserService {

    GetUserDTO getUser(String userId);

}
