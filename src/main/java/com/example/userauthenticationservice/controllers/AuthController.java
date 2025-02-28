package com.example.userauthenticationservice.controllers;

import com.example.userauthenticationservice.dtos.LoginDto;
import com.example.userauthenticationservice.dtos.SignupDto;
import com.example.userauthenticationservice.dtos.UserDto;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.IAuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto){

        Pair<User, MultiValueMap<String, String>> pair = authService.login(loginDto.getEmail(), loginDto.getPassword());

        UserDto userDto = from(pair.a);
        MultiValueMap<String, String> header = pair.b;
        return new ResponseEntity<>(userDto,header, HttpStatusCode.valueOf(200));
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
