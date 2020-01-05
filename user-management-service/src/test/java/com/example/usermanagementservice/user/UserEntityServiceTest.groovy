package com.example.usermanagementservice.user

import com.example.usermanagementservice.user.exception.ResourceNotFoundException
import spock.lang.Specification
import spock.lang.Subject

class UserEntityServiceTest extends Specification {

    def mockUserRepository = Mock(UserRepository.class)
    def mockUserMapper = Mock(UserMapper.class)
    @Subject
    UserService userService = new UserService(mockUserRepository, mockUserMapper)

    def "should save a user to the database"() {
        given:
        UserDto userDto = UserDto.builder().build()
        UserEntity userEntity = new UserEntity()

        when:
        def savedUser = userService.save(userDto)

        then:
        1 * mockUserMapper.toEntity(userDto) >> userEntity
        1 * mockUserRepository.save(userEntity) >> {
            def mockUser = new UserEntity()
            mockUser.setId("1")
            mockUser
        }
        1 * mockUserMapper.toDto(_ as UserEntity) >> { args -> UserDto.builder().id(args.id as String).build() }

        expect:
        savedUser.id != null
    }

    def "should update an existing user"() {
        given:
        UserDto userDto = UserDto.builder().id("1").name("name").nameAr("nameAr").build()
        UserEntity userEntity = new UserEntity()
        userEntity.setId(userDto.id)
        userEntity.setName("NAME")
        userEntity.setNameAr("NAME_AR")

        when:
        def savedUser = userService.update(userDto)

        then:
        1 * mockUserRepository.findById(userDto.id) >> Optional.of(userEntity)
        1 * mockUserRepository.save(userEntity) >> { args ->
            userEntity.name == 'name'
            userEntity.nameAr == 'nameAr'
            userEntity
        }
        1 * mockUserMapper.toDto(_ as UserEntity) >> { args ->
            def u = args[0] as UserEntity
            UserDto.builder()
                    .id(u.id as String)
                    .name(u.name as String)
                    .nameAr(u.nameAr as String)
                    .build()
        }

        expect:
        savedUser.name == 'name'
        savedUser.nameAr == 'nameAr'
    }

    def "should throw a ResourceNotFoundException if user does not exist"() {
        given:
        UserDto userDto = UserDto.builder().id("ID").build()

        when:
        userService.update(userDto)

        then:
        1 * mockUserRepository.findById(userDto.id) >> Optional.empty()
        thrown(ResourceNotFoundException)
    }

//    def "should get a user from the database with the given id"() {
//        given:
//        def userId = '1'
//
//        when:
//        def user = userService.get(userId)
//
//        then:
//        1 * mockUserRepository.findById("1") >> {
//            def mockUser = new UserEntity()
//            mockUser.setId("1")
//            Optional.of(mockUser)
//        }
//
//        expect:
//        user.id == "1"
//    }
}
