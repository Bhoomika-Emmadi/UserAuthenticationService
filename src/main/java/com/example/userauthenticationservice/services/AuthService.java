package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.exceptions.IncorrectPasswordException;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistException;
import com.example.userauthenticationservice.exceptions.UserDoesntExistException;
import com.example.userauthenticationservice.models.Status;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User login(String email, String password) {
        Optional<User> userOptional = userRepo.getUserByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserDoesntExistException("Incorrect email id, please check and login again");
        }
//        if(!userOptional.get().getPassword().equals(password)){ //this works for non encoded passwords
//        if(bCryptPasswordEncoder.encode(password).matches(userOptional.get().getPassword())){ //this also works for ecoded password
        if(bCryptPasswordEncoder.matches(password,userOptional.get().getPassword())){
            throw new IncorrectPasswordException("Password is incorrect please check and re-login");
        }
        return userOptional.get();
    }
}
