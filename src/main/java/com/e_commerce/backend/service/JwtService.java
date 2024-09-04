package com.e_commerce.backend.service;

import com.e_commerce.backend.Repository.UserRepository;
import com.e_commerce.backend.enity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private String SECRET_KEY;

    @Autowired
    private UserRepository userRepository;

    public JwtService() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            SECRET_KEY = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        UserEntity user = userRepository.findByUsername(username);

        claims.put("username", user.getUsername());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("role", user.getRole());
        claims.put("isEmailVerified", user.isEmailVerified());
        claims.put("accountStatus", user.getAccountStatus());
        claims.put("addressLine1", user.getAddressLine1());
        claims.put("addressLine2", user.getAddressLine2());
        claims.put("city", user.getCity());
        claims.put("state", user.getState());
        claims.put("postalCode", user.getPostalCode());
        claims.put("country", user.getCountry());

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 500))
                .and()
                .signWith(getKey())
                .compact();

    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
