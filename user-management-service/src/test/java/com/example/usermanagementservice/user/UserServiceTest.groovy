package com.example.usermanagementservice.user

import spock.lang.Specification
import spock.lang.Subject

class UserServiceTest extends Specification {

    def mockUserRepository = Mock(UserRepository.class)
    @Subject
    UserService userService = new UserService(mockUserRepository)

    def "should get a user from the database with the given id"() {
        given:
        def userId = '1'

        when:
        def user = userService.get(userId)

        then:
        1 * mockUserRepository.findById("1") >> {
            def mockUser = new User()
            mockUser.setId("1")
            Optional.of(mockUser)
        }

        expect:
        user.id == "1"
    }
}
