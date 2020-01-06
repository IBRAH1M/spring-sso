package com.example.usermanagementservice.user;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String nameAr;
}
