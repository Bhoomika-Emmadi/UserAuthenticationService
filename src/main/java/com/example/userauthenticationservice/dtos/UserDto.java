package com.example.userauthenticationservice.dtos;

import com.example.userauthenticationservice.models.Roles;
import com.example.userauthenticationservice.models.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Getter@Setter
public class UserDto {
    private Long id;
    private String email;
    private Date createdAt;
    private Date lastUpdatedAt;
    private Status status;
//    private List<Roles> roles;
}
