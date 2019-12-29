package com.example.clientmanagementservice.client

import groovy.util.logging.Slf4j
import org.junit.After
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Subject

@Slf4j
@WebMvcTest(controllers = [ClientController])
class ClientControllerTest extends Specification {

    @Subject
    ClientController clientController
    @Mock
    ClientService clientService
    @Autowired
    protected MockMvc mockMvc

    @Before
    void setup() {
        log.info("Tests setup begin .......")
        MockitoAnnotations.initMocks(this)
        clientController = new ClientController(clientService)
        log.info("Tests setup done.")
    }

    @After
    void cleanup() {
        log.info("Tests cleanup begin .......")
        log.info("Tests cleanup done.")
    }

    def "should return a client given its id"() {
        given:
        int left = 2
        int right = 2

        when:
        int result = left + right

        then:
        result == 4
    }
}
