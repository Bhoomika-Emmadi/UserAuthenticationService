package com.example.userauthenticationservice.springoauth;

import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optionaluser =  userRepository.getUserByEmail(email);
        if(optionaluser.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }

        return new CustomUserDetails(optionaluser.get());
    }
}
