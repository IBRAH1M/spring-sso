package com.example.usermanagementservice.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

interface UserRepository extends PagingAndSortingRepository<UserEntity, String> {

    @Query("SELECT user FROM #{#entityName} user WHERE (user.name LIKE %:q% OR user.nameAr LIKE %:q%)")
    Page<UserEntity> findBySearchQuery(Pageable pageable, @Param("q") String searchQuery);
}
