package com.example.userauthenticationservice.services;


import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepo;

    public User getUserDetails(Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        if(userOptional.isEmpty())  {return null;}
        return userOptional.get();
    }
}
