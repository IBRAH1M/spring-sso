package com.example.usermanagementservice.user;

import org.springframework.data.repository.PagingAndSortingRepository;

interface UserRepository extends PagingAndSortingRepository<User, String> {
}
