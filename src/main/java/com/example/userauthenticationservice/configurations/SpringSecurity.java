package com.example.userauthenticationservice.configurations;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

@Configuration
public class SpringSecurity {

    // need this function to disable the login which comes with default spring security dependency, and to permit all the incoming requests
    //commenting this for the Oauth usage as this bean is conflicting with the other beans in SecurityConfig class
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.cors().disable();
//        httpSecurity.csrf().disable();
//        httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
//        return httpSecurity.build();
//    }

    //this is required to use create a singleton obj of BcryptPasswordEncoder by the spring container using @Autowired in service layer
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretKey getSecretKey()
    {
        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();
        return secretKey;
    }
}
