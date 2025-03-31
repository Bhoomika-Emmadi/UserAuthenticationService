package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.models.User;

public interface IUserService {

    User getUserDetails(Long id);
}
