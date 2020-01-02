package com.example.usermanagementservice.user;

import org.mapstruct.Mapper;

@Mapper
interface UserMapper {

    UserDto toDro(UserEntity user);

    UserEntity toEntity(UserDto userDto);
}
