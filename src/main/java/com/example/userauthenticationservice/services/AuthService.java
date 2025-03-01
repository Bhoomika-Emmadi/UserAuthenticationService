package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.exceptions.IncorrectPasswordException;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistException;
import com.example.userauthenticationservice.exceptions.UserDoesntExistException;
import com.example.userauthenticationservice.models.Status;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.models.UserSession;
import com.example.userauthenticationservice.repos.SessionRepo;
import com.example.userauthenticationservice.repos.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService{

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private SecretKey secretKey;

    public User signUp(String email, String password) {
        Optional<User> userOptional = userRepo.getUserByEmail(email);
        if(userOptional.isPresent()){
             throw new UserAlreadyExistException("Please try with new emailId");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setStatus(Status.ACTIVE);
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
//        if(bCryptPasswordEncoder.matches(password,userOptional.get().getPassword())){
//            throw new IncorrectPasswordException("Password is incorrect please check and re-login");
//        }

        if(bCryptPasswordEncoder.encode(password).matches(userOptional.get().getPassword())){
            throw new IncorrectPasswordException(" Password is incorrect please check and re-login");
        }



//        String message = " { \n" +
//                "\"email\": \"bhoomika\" \n" +
//                        " }";
//        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);


        Map<String,Object> userClaims = new HashMap<>();
        userClaims.put("userId",userOptional.get().getId());
        userClaims.put("permissions",userOptional.get().getRoles());
        Long currentTimeInMillis = System.currentTimeMillis();
        userClaims.put("iat",currentTimeInMillis);
        userClaims.put("exp",currentTimeInMillis+8640000);
        userClaims.put("issuer","scaler");


//        MacAlgorithm algorithm = Jwts.SIG.HS256;
//        SecretKey secretKey = algorithm.key().build();


        String token = Jwts.builder().claims(userClaims).signWith(secretKey).compact();

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(HttpHeaders.SET_COOKIE, token);

        //For Validation purpose
        UserSession session = new UserSession();
        session.setToken(token);
        session.setUser(userOptional.get());
        session.setStatus(Status.ACTIVE);
        sessionRepo.save(session);


        Pair<User, MultiValueMap<String,String>> pair=new Pair<>(userOptional.get(), header);

        return pair;

    }

//    public boolean validateToken(Long userId, String token) {
//
//        Optional<UserSession> optionalSession = sessionRepo.findByUserIdAndToken(userId, token);
//        if(optionalSession.isEmpty()){
//            throw new UserDoesntExistException("User not found");
//        }
//
//        UserSession userSession = new UserSession();
//        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
//        Claims claims = jwtParser.parseSignedClaims(token).getPayload();
//        Long expiryStoredInToken = (Long)claims.get("exp");
//        Long currentTime =System.currentTimeMillis();
//
//        System.out.println(expiryStoredInToken);
//        System.out.println(currentTime);
//
//        if(currentTime > expiryStoredInToken) {
//            userSession.setStatus(Status.INACTIVE);
//            sessionRepo.save(userSession);
//            return false;
//        }
//
//        return true;
//    }


    public Boolean validateToken(Long userId, String token) {
        Optional<UserSession> optionalUserSession = sessionRepo.findByTokenAndUser_Id(token,userId);

        if(optionalUserSession.isEmpty()) return false;

        UserSession userSession = optionalUserSession.get();

        String persistedToken = userSession.getToken();

        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(persistedToken).getPayload();
        Long expiryStoredInToken = (Long)claims.get("exp");
        Long currentTime =System.currentTimeMillis();

        System.out.println(expiryStoredInToken);
        System.out.println(currentTime);

        if(currentTime > expiryStoredInToken) {
            userSession.setStatus(Status.INACTIVE);
            sessionRepo.save(userSession);
            return false;
        }

        return true;
    }
}
