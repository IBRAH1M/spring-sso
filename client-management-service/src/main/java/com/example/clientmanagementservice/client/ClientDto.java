package com.example.clientmanagementservice.client;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
public class ClientDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String nameAr;
}
