package com.e_commerce.backend.utils;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
@Service
public class PasswordGenerator {

    public String generatePassword(int characters){
        StringBuilder password = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        String charSet = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM!@#$%^&*()";
        for (int i = 0; i < characters; i++) {
            password.append(charSet.charAt(secureRandom.nextInt(charSet.length())));
        }
        System.out.println("Password is "+password);
        return password.toString();
    }
}