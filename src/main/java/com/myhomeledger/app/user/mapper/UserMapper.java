package com.myhomeledger.app.user.mapper;

import com.myhomeledger.app.user.dto.CreateUserDTO;
import com.myhomeledger.app.user.dto.GetUserDTO;
import com.myhomeledger.app.user.dto.UpdateUserDTO;
import com.myhomeledger.app.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring", imports = {Instant.class})
public interface UserMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", expression = "java(Instant.now())")
    @Mapping(target = "updatedAt", expression = "java(Instant.now())")
    @Mapping(target = "userName", source = "username")
    UserEntity toEntity(CreateUserDTO dto);

    @Mapping(target = "username", source = "userName")
    GetUserDTO toDTO(UserEntity entity);
}

