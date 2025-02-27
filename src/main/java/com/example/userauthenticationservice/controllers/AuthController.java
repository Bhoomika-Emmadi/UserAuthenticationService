package com.example.userauthenticationservice.controllers;

import com.example.userauthenticationservice.dtos.LoginDto;
import com.example.userauthenticationservice.dtos.SignupDto;
import com.example.userauthenticationservice.dtos.UserDto;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    IAuthService authService;

    //sign up, login

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignupDto signupDto){
        User user = authService.signUp(signupDto.getEmail(), signupDto.getPassword());
        return from(user);
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody LoginDto loginDto){

        User user = authService.login(loginDto.getEmail(), loginDto.getPassword());
        return from(user);
    }

    private UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setStatus(user.getStatus());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setRoles(user.getRoles());
        userDto.setLastUpdatedAt(user.getLastUpdatedAt());
        return userDto;
    }
}
