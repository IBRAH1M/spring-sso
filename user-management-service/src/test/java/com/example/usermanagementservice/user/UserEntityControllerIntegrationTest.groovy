package com.example.usermanagementservice.user

import groovy.json.JsonSlurper
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class UserControllerIntegrationTest extends Specification {

    def baseApiUrl = '/api/v1/users'
    def mockUserService = Mock(UserService.class)
    @Subject
    UserController userController = new UserController(mockUserService)
    MockMvc mockMvc = standaloneSetup(userController).build()

    def "should return a user given its id"() {
        given:
        def userId = '1'

        when:
        def response = mockMvc.perform(get("$baseApiUrl/$userId")).andReturn().getResponse()
        def content = new JsonSlurper().parseText(response.getContentAsString())

        then:
        1 * mockUserService.get("1") >> {
            def mockUser = new UserDto()
            mockUser.setId("1")
            mockUser
        }
        response.status == 200
        content.id == "1"
    }

    def "should return a list of users"() {
        given:
        def role = "ADMIN"

        when:
        def response = mockMvc.perform(get("$baseApiUrl?role=$role")).andReturn().getResponse()
        def content = new JsonSlurper().parseText(response.getContentAsString())

        then:
        1 * mockUserService.getAll("ADMIN") >> {
            def users = new ArrayList()
            def u = new UserDto()
            u.id = 1
            users.add(u)
            users
        }
        response.status == 200
        content[0].id == '1'
    }
}
