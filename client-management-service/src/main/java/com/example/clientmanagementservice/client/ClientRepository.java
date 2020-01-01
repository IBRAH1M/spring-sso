package com.example.clientmanagementservice.client;

import org.springframework.data.repository.PagingAndSortingRepository;

interface ClientRepository extends PagingAndSortingRepository<Client, String> {
}
