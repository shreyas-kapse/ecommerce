package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.dtos.LoginDTO;
import com.e_commerce.backend.dtos.RegisterUserDTO;

public interface ILoginService {
    public DefaultResponse loginUser(LoginDTO user);

    public DefaultResponse registerUser(RegisterUserDTO registerUserDTO);
}