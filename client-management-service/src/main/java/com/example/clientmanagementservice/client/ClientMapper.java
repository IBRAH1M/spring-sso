package com.example.clientmanagementservice.client;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ClientEntity.class, ClientDto.class})
interface ClientMapper {

    ClientEntity toEntity(ClientDto clientDto);

    ClientDto toDto(ClientEntity clientEntity);
}
