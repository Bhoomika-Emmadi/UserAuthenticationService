package com.example.userauthenticationservice.controllers;

import com.example.userauthenticationservice.dtos.UserDto;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/users/{id}")
    public UserDto getUserDetails(@PathVariable Long id) {
        User user = userService.getUserDetails(id);
        return from(user);
    }

    public UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        //userDto.setRoles(user.getRoles());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
