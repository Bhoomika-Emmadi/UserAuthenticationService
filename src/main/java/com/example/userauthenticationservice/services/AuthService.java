package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.exceptions.IncorrectPasswordException;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistException;
import com.example.userauthenticationservice.exceptions.UserDoesntExistException;
import com.example.userauthenticationservice.models.Status;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.UserRepository;
import io.jsonwebtoken.Jwts;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService implements IAuthService{

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepo;

    @Override
    public User signUp(String email, String password) {
        Optional<User> userOptional = userRepo.getUserByEmail(email);
        if(userOptional.isPresent()){
             throw new UserAlreadyExistException("Please try with new emailId");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setStatus(Status.INACTIVE);
        user.setCreatedAt(new Date());
        user.setLastUpdatedAt(new Date());
        userRepo.save(user);
        Optional<User> userFromDB = userRepo.getUserByEmail(email);
        user.setId(userFromDB.get().getId());

        return user;
    }

    @Override
    public Pair<User, MultiValueMap<String, String>> login(String email, String password) {
        Optional<User> userOptional = userRepo.getUserByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserDoesntExistException("Incorrect email id, please check and login again");
        }
//        if(!userOptional.get().getPassword().equals(password)){ //this works for non encoded passwords
//        if(bCryptPasswordEncoder.encode(password).matches(userOptional.get().getPassword())){ //this also works for ecoded password
        if(bCryptPasswordEncoder.matches(password,userOptional.get().getPassword())){
            throw new IncorrectPasswordException("Password is incorrect please check and re-login");
        }


        String message = " { \n" +
                "\"email\": \"bhoomika\" \n" +
                        " }";
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        String token = Jwts.builder().content(bytes).compact();

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(HttpHeaders.SET_COOKIE, token);


        Pair<User, MultiValueMap<String,String>> pair=new Pair<>(userOptional.get(), header);

        return pair;

    }
}
