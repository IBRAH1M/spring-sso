package com.example.clientmanagementservice.client;

import org.mapstruct.Mapper;

@Mapper
interface ClientMapper {

    ClientEntity toEntity(ClientDto clientDto);

    ClientDto toDto(ClientEntity clientEntity);
}
