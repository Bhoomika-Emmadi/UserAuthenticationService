package com.example.userauthenticationservice.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class LoginDto {

    private String email;
    private String password;
}
