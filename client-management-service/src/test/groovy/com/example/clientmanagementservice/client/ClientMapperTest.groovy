package com.example.clientmanagementservice.client

import org.mapstruct.factory.Mappers
import spock.lang.Specification
import spock.lang.Subject

class ClientMapperTest extends Specification {

    @Subject
    ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class)

    def "should convert from entity to dto"(){
        given:
        def clientEntity = new ClientEntity()
        clientEntity.id = "ID"
        clientEntity.name = "NAME"
        clientEntity.nameAr = "NAME_AR"

        when:
        def clientDto = clientMapper.toDto(clientEntity)

        then:
        assert clientDto instanceof ClientDto
        clientDto.id == "ID"
        clientDto.name == "NAME"
        clientDto.nameAr == "NAME_AR"
    }

    def "should convert from dto to entity"(){
        given:
        def clientDto = ClientDto.builder().id("ID").name("NAME").nameAr("NAME_AR").build()

        when:
        def clientEntity = clientMapper.toEntity(clientDto)

        then:
        assert clientEntity instanceof ClientEntity
        clientEntity.id == "ID"
        clientEntity.name == "NAME"
        clientEntity.nameAr == "NAME_AR"
    }
}
