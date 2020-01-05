package com.example.usermanagementservice.user;

import org.mapstruct.Mapper;

@Mapper
interface UserMapper {

    UserDto toDto(UserEntity user);

    UserEntity toEntity(UserDto userDto);
}
