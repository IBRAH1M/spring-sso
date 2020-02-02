package com.example.usermanagementservice.client;

import lombok.Data;

import java.io.Serializable;

@Data
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
}
