package com.example.usermanagementservice.user;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String nameAr;
}
