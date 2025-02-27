package com.example.userauthenticationservice.dtos;


import com.example.userauthenticationservice.models.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
public class SignupDto {

    private String email;
    private String password;
}
