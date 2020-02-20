package com.example.clientmanagementservice

import groovy.json.JsonSlurper
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
class ClientManagementServiceE2ETests extends Specification {

    def baseApiUrl = '/api/v1/clients'

    @Autowired
    protected MockMvc mockMvc

    def "should create a new client"() {
        expect: 'A new client created and returned'
        mockMvc.perform(post("$baseApiUrl/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content('{"id":"", "name":"","nameAr":""}'))
                .andExpect(status().isCreated())
                .andExpect(jsonPath('$.id').isNotEmpty())
                .andExpect(jsonPath('$.name').value(""))
                .andExpect(jsonPath('$.nameAr').value(""))
    }

    def "should update an existing client"() {
        given:
        def client = new JsonSlurper().parseText(mockMvc.perform(post("$baseApiUrl/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content('{"id":"", "name":"","nameAr":""}'))
                .andReturn().getResponse().contentAsString)

        expect: 'A existing client updated and returned'
        mockMvc.perform(put("$baseApiUrl/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(/{"id": "$client.id", "name":"NAME","nameAr":"NAME_AR"}/))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name').value("NAME"))
                .andExpect(jsonPath('$.nameAr').value("NAME_AR"))
    }

    def "should retrieve a existing client by id"() {
        given:
        def client = new JsonSlurper().parseText(mockMvc.perform(post("$baseApiUrl/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content('{"id":"", "name":"NAME","nameAr":"NAME_AR"}'))
                .andReturn().getResponse().contentAsString)

        expect: 'A existing client with the given id returned'
        mockMvc.perform(get("$baseApiUrl/$client.id")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.id').value(client['id']))
                .andExpect(jsonPath('$.name').value("NAME"))
                .andExpect(jsonPath('$.nameAr').value("NAME_AR"))
    }

    def "should retrieve a filtered clients page"() {
        given:
        mockMvc.perform(post("$baseApiUrl/").contentType(MediaType.APPLICATION_JSON_VALUE).content('{"id":"", "name":"NAME1","nameAr":""}'))
        mockMvc.perform(post("$baseApiUrl/").contentType(MediaType.APPLICATION_JSON_VALUE).content('{"id":"", "name":"NAME2","nameAr":""}'))
        mockMvc.perform(post("$baseApiUrl/").contentType(MediaType.APPLICATION_JSON_VALUE).content('{"id":"", "name":"NAME3","nameAr":""}'))

        expect: 'Any client with "1" in its name returned'
        mockMvc.perform(get("$baseApiUrl?page=0&size=10&sort=id,desc&q=1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.totalElements').value(1))
                .andExpect(jsonPath('$.pageable.sort.sorted').value(true))
                .andExpect(jsonPath('$.content[0].name').value("NAME1"))
    }

    def "should delete an existing client by id"() {
        given:
        def client = new JsonSlurper().parseText(mockMvc.perform(post("$baseApiUrl/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content('{"id":"", "name":"","nameAr":""}'))
                .andReturn().getResponse().contentAsString)

        expect: 'A existing client delete'
        mockMvc.perform(delete("$baseApiUrl/$client.id")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())

        mockMvc.perform(get("$baseApiUrl/$client.id")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
    }
}
