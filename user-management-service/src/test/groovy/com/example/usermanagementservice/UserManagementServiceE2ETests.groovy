package com.example.usermanagementservice

import groovy.json.JsonSlurper
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserManagementServiceE2ETests extends Specification {

    def final BASE_API_URL = '/api/v1/users'

    @Autowired
    protected MockMvc mockMvc

    @Test
    def "should create a new user"() {
        given:
        def client = new JsonSlurper().parseText(mockMvc.perform(post("$STUB_CLIENTS_URL/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getClientPayload("TEST_ID")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().contentAsString)

        expect: 'A new user created and returned'
        mockMvc.perform(post("$BASE_API_URL/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getUserPayload("", "", "", "$client.id")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath('$.id').isNotEmpty())
                .andExpect(jsonPath('$.name').value(""))
                .andExpect(jsonPath('$.nameAr').value(""))
                .andExpect(jsonPath('$.clientId').value("TEST_ID"))
    }

    @Test
    def "should update an existing user"() {
        given:
        def user = new JsonSlurper().parseText(mockMvc.perform(post("$BASE_API_URL/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getUserPayload("", "", "", "TEST_ID")))
                .andReturn().getResponse().contentAsString)

        expect: 'A existing user updated and returned'
        mockMvc.perform(put("$BASE_API_URL/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getUserPayload(user.id, "NAME", "NAME_AR", "TEST_ID")))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name').value("NAME"))
                .andExpect(jsonPath('$.nameAr').value("NAME_AR"))
                .andExpect(jsonPath('$.clientId').value("TEST_ID"))
    }

    @Test
    def "should retrieve a existing user by id"() {
        given:
        def user = new JsonSlurper().parseText(mockMvc.perform(post("$BASE_API_URL/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content('{"id":"", "name":"NAME","nameAr":"NAME_AR","clientId":"TEST_ID"}'))
                .andReturn().getResponse().contentAsString)

        expect: 'A existing user with the given id returned'
        mockMvc.perform(get("$BASE_API_URL/$user.id")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.id').value(user['id']))
                .andExpect(jsonPath('$.name').value("NAME"))
                .andExpect(jsonPath('$.nameAr').value("NAME_AR"))
                .andExpect(jsonPath('$.clientId').value("TEST_ID"))
    }

    @Test
    def "should retrieve a filtered users page"() {
        given:
        mockMvc.perform(post("$BASE_API_URL/").contentType(MediaType.APPLICATION_JSON_VALUE).content('{"id":"", "name":"NAME1","nameAr":"","clientId":"TEST_ID"}'))
        mockMvc.perform(post("$BASE_API_URL/").contentType(MediaType.APPLICATION_JSON_VALUE).content('{"id":"", "name":"NAME2","nameAr":"","clientId":"TEST_ID"}'))
        mockMvc.perform(post("$BASE_API_URL/").contentType(MediaType.APPLICATION_JSON_VALUE).content('{"id":"", "name":"NAME3","nameAr":"","clientId":"TEST_ID"}'))

        expect: 'Any user with "1" in its name returned'
        mockMvc.perform(get("$BASE_API_URL?page=0&size=10&sort=id,desc&q=1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.totalElements').value(1))
                .andExpect(jsonPath('$.pageable.sort.sorted').value(true))
                .andExpect(jsonPath('$.content[0].name').value("NAME1"))
                .andExpect(jsonPath('$.content[0].clientId').value("TEST_ID"))
    }

    @Test
    def "should delete an existing user by id"() {
        given:
        def user = new JsonSlurper().parseText(mockMvc.perform(post("$BASE_API_URL/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content('{"id":"", "name":"","nameAr":"","clientId":"TEST_ID"}'))
                .andReturn().getResponse().contentAsString)

        expect: 'A existing user delete'
        mockMvc.perform(delete("$BASE_API_URL/$user.id")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())

        mockMvc.perform(get("$BASE_API_URL/$user.id")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
    }

    private static String getUserPayload(String id, String name, String nameAr, String clientId) {
        """{"id":"${id}", "name":"${name}","nameAr":"${nameAr}","clientId":"${clientId}"}"""
    }

    private static String getClientPayload(String id) {
        """{"id":"${id}"}"""
    }
}
