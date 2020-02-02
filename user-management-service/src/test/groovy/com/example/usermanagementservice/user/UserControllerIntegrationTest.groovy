package com.example.usermanagementservice.user

import com.example.usermanagementservice.user.exception.ResourceNotFoundException
import groovy.json.JsonSlurper
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.http.HttpStatus.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class UserControllerIntegrationTest extends Specification {

    private String baseApiUrl = '/api/v1/users'
    private MockMvc mockMvc
    private UserService mockUserService
    @Subject
    private UserController userController

    def setup() {
        mockUserService = Mock(UserService.class)
        userController = new UserController(mockUserService)
        mockMvc = standaloneSetup(userController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build()
    }

    def "should save a user"() {
        given:
        def userToSave = '{"id":"", "name":"", "nameAr":""}'

        when:
        def response = mockMvc.perform(post("$baseApiUrl/")
                .content(userToSave)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        1 * mockUserService.save(_) >> UserDto.builder().id("1").build()

        expect:
        response.status == CREATED.value()
        content.id == "1"
    }

    def "should not save a user when id is given in the request"() {
        given:
        def userToSave = '{"id":"ID"}'

        when:
        def response = mockMvc.perform(post("$baseApiUrl/")
                .content(userToSave)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        0 * mockUserService.save(_)

        expect:
        response.status == BAD_REQUEST.value()
    }

    def "should not save user when empty json is given in the request"() {
        given:
        def userToSave = '{}'

        when:
        def response = mockMvc.perform(post("$baseApiUrl/")
                .content(userToSave)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        0 * mockUserService.save(_)

        expect:
        response.status == BAD_REQUEST.value()
    }

    def "should update a user"() {
        given:
        def userToUpdate = '{"id":"1", "name":"NAME", "nameAr":"NAME_AR"}'

        when:
        def response = mockMvc.perform(put("$baseApiUrl/")
                .content(userToUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        1 * mockUserService.update(_) >> UserDto.builder().id("1").name("NAME").nameAr("NAME_AR").build()

        expect:
        response.status == OK.value()
        content.id == "1"
        content.name == "NAME"
        content.nameAr == "NAME_AR"
    }

    def "should not update a user when id isn't given in the request"() {
        given:
        def userToUpdate = '{"id":""}'

        when:
        def response = mockMvc.perform(put("$baseApiUrl/")
                .content(userToUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        0 * mockUserService.update(_)

        expect:
        response.status == BAD_REQUEST.value()
    }

    def "should not update a user when id does not exist"() {
        given:
        def userToUpdate = '{"id":"ID", "name":"", "nameAr":""}'

        when:
        def response = mockMvc.perform(put("$baseApiUrl/")
                .content(userToUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockUserService.update(_) >> { throw new ResourceNotFoundException() }

        expect:
        response.status == NOT_FOUND.value()
    }

    def "should not update user when empty json is given in the request"() {
        given:
        def userToUpdate = '{}'

        when:
        def response = mockMvc.perform(put("$baseApiUrl/")
                .content(userToUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        0 * mockUserService.update(_)

        expect:
        response.status == BAD_REQUEST.value()
    }

    def "should return a user given its id"() {
        given:
        def userId = "1"

        when:
        def response = mockMvc.perform(get("$baseApiUrl/$userId")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        1 * mockUserService.get("1") >> UserDto.builder().id("1").build()

        expect:
        response.status == OK.value()
        content.id == "1"
    }

    def "should return not found 404 if user id does not exist"() {
        given:
        def userId = "1"

        when:
        def response = mockMvc.perform(get("$baseApiUrl/$userId")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockUserService.get("1") >> { throw new ResourceNotFoundException() }

        expect:
        response.status == NOT_FOUND.value()
    }

    def "should return a users page"() {
        when:
        def response = mockMvc.perform(get("$baseApiUrl")
                .queryParam("page", "1")
                .queryParam("size", "10")
                .queryParam("sort", "id,desc")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockUserService.getAll(_ as Pageable, "") >> { new PageImpl<>(List.of(UserDto.builder().build())) }

        expect:
        response.status == OK.value()
    }

    def "should return a users page filter by search query"() {
        when:
        def response = mockMvc.perform(get("$baseApiUrl")
                .queryParam("q", "test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockUserService.getAll(_ as Pageable, "test") >> { new PageImpl<>(List.of(new UserEntity())) }

        expect:
        response.status == OK.value()
    }

    def "should delete a user given its id"() {
        given:
        def userToDelete = '1'

        when:
        def response = mockMvc.perform(delete("$baseApiUrl/$userToDelete")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockUserService.get("1")
        1 * mockUserService.delete(_)

        expect:
        response.status == OK.value()
    }

    def "should not delete a user when id does not exist"() {
        given:
        def userToDelete = 'ID'

        when:
        def response = mockMvc.perform(delete("$baseApiUrl/$userToDelete")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockUserService.get("ID") >> { throw new ResourceNotFoundException() }
        0 * mockUserService.delete(_)

        expect:
        response.status == NOT_FOUND.value()
    }
}