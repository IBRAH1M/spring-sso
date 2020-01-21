package com.example.usermanagementservice.user

import org.mapstruct.factory.Mappers
import spock.lang.Specification
import spock.lang.Subject

class UserMapperTest extends Specification {

    @Subject
    UserMapper userMapper = Mappers.getMapper(UserMapper.class)

    def "should convert from entity to dto"() {
        given:
        def userEntity = new UserEntity()
        userEntity.id = "ID"
        userEntity.name = "NAME"
        userEntity.nameAr = "NAME_AR"

        when:
        def userDto = userMapper.toDto(userEntity)

        then:
        assert userDto instanceof UserDto
        userDto.id == "ID"
        userDto.name == "NAME"
        userDto.nameAr == "NAME_AR"
    }

    def "should convert from dto to entity"() {
        given:
        def userDto = UserDto.builder().id("ID").name("NAME").nameAr("NAME_AR").build()

        when:
        def userEntity = userMapper.toEntity(userDto)

        then:
        assert userEntity instanceof UserEntity
        userEntity.id == "ID"
        userEntity.name == "NAME"
        userEntity.nameAr == "NAME_AR"
    }
}