package com.example.usermanagementservice.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
}
