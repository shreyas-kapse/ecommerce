package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.enity.UserEntity;

public interface ILoginService {
    public DefaultResponse loginUser(UserEntity user);

    public DefaultResponse registerUser(RegisterUserDTO registerUserDTO);
}