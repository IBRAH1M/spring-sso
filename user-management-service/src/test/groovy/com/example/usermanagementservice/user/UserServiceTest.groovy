package com.example.usermanagementservice.user

import com.example.usermanagementservice.client.Client
import com.example.usermanagementservice.client.ClientService
import com.example.usermanagementservice.exception.ResourceNotFoundException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject

class UserServiceTest extends Specification {

    UserRepository mockUserRepository
    UserMapper mockUserMapper
    ClientService mockClientService
    @Subject
    UserService userService

    void setup() {
        mockUserRepository = Mock(UserRepository.class)
        mockUserMapper = Mock(UserMapper.class)
        mockClientService = Mock(ClientService.class)
        userService = new UserService(mockUserRepository, mockUserMapper, mockClientService)
    }

    def "should save a user to the database"() {
        given:
        UserDto userDto = UserDto.builder().clientId("CLIENT_ID").build()
        UserEntity userEntity = new UserEntity()

        when:
        def savedUser = userService.save(userDto)

        then:
        1 * mockClientService.getClient("CLIENT_ID") >> Client.builder().build()
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

    def "should not save a user if client does not exists"() {
        given:
        UserDto userDto = UserDto.builder().clientId("CLIENT_ID").build()

        when:
        userService.save(userDto)

        then:
        1 * mockClientService.getClient(_) >> {throw new ResourceNotFoundException()}
        0 * mockUserMapper.toEntity(_)
        0 * mockUserRepository.save(_)
        0 * mockUserMapper.toDto(_)
        thrown(ResourceNotFoundException)
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
            UserDto.builder().id(u.id).name(u.name).nameAr(u.nameAr).build()
        }

        expect:
        savedUser.name == 'name'
        savedUser.nameAr == 'nameAr'
    }

    def "should get a user from the database given its id"() {
        given:
        def userId = '1'

        when:
        def user = userService.get(userId)

        then:
        1 * mockUserRepository.findById("1") >> {
            def mockUser = new UserEntity()
            mockUser.setId("1")
            mockUser.setName("NAME")
            mockUser.setNameAr("NAME_AR")
            Optional.of(mockUser)
        }
        1 * mockUserMapper.toDto(_ as UserEntity) >> { args ->
            def c = args[0] as UserEntity
            UserDto.builder().id(c.id).name(c.name).nameAr(c.nameAr).build()
        }

        expect:
        user.id == "1"
    }

    def "should throw ResourceNotFoundException if user id does not exist"() {
        given:
        def userId = '1'

        when:
        userService.get(userId)

        then:
        1 * mockUserRepository.findById("1") >> Optional.empty()
        thrown(ResourceNotFoundException)
    }

    def "should get a users page"() {
        given:
        def pageable = PageRequest.of(0, 2)
        def user1 = new UserEntity()
        user1.setId("1")
        user1.setName("NAME")
        user1.setNameAr("NAME_AR")
        def user2 = new UserEntity()
        user2.setId("2")
        user2.setName("NAME2")
        user2.setNameAr("NAME_AR2")

        when:
        def usersPage = userService.getAll(pageable, "")

        then:
        1 * mockUserRepository.findAll(pageable) >> {
            new PageImpl<>(List.of(user1, user2))
        }
        2 * mockUserMapper.toDto(_ as UserEntity) >> { args ->
            def c = args[0] as UserEntity
            UserDto.builder().id(c.id).name(c.name).nameAr(c.nameAr).build()
        }

        expect:
        usersPage.size == 2
        usersPage.totalElements == 2
    }

    def "should get a users page filtered by search query"() {
        given:
        def pageable = PageRequest.of(0, 2)
        def user1 = new UserEntity()
        user1.setId("1")
        user1.setName("NAME")
        user1.setNameAr("NAME_AR")
        def user2 = new UserEntity()
        user2.setId("2")
        user2.setName("NAME2")
        user2.setNameAr("NAME_AR2")

        when:
        def usersPage = userService.getAll(pageable, "NAME")

        then:
        1 * mockUserRepository.findBySearchQuery(pageable, "NAME") >> {
            new PageImpl<>(List.of(user1))
        }
        1 * mockUserMapper.toDto(_ as UserEntity) >> { args ->
            def c = args[0] as UserEntity
            UserDto.builder().id(c.id).name(c.name).nameAr(c.nameAr).build()
        }

        expect:
        usersPage.size == 1
        usersPage.totalElements == 1
    }

    def "should delete an existing user"() {
        given:
        def user = UserDto.builder().id("1").build()
        def userEntity = new UserEntity()

        when:
        userService.delete(user)

        then:
        1 * mockUserRepository.findById(user.id) >> Optional.of(userEntity)
        1 * mockUserRepository.delete(userEntity)
    }
}