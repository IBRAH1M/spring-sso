package com.example.clientmanagementservice.client;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ClientEntity.class, ClientDto.class})
interface ClientMapper {

    ClientEntity toEntity(ClientDto clientDto);

    ClientDto toDto(ClientEntity clientEntity);
}
