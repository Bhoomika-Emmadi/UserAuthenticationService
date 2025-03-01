package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.models.User;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.util.MultiValueMap;

public interface IAuthService {
    User signUp(String email, String password);
    Pair<User, MultiValueMap<String, String>> login(String email, String password);
    Boolean validateToken(Long userId, String token);
}
