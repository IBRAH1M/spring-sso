package com.example.usermanagementservice.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.DoubleStream;

interface UserRepository extends PagingAndSortingRepository<UserEntity, String> {

//    @Query(value = "SELECT * FROM #ENRIRY")
    Page<UserEntity> findBySearchQuery(@Param("q") String searchQuery, Pageable pageable);
}
