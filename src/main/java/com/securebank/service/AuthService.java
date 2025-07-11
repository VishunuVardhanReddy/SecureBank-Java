package com.securebank.service;

import com.securebank.dto.AuthResponse;
import com.securebank.exception.BankException;
import com.securebank.model.User;
import com.securebank.repository.UserRepository;
import com.securebank.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private UserRepository userRepository;
    
    public AuthResponse authenticateUser(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            String jwt = tokenProvider.generateToken(authentication);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BankException("User not found"));
            
            AuthResponse response = new AuthResponse();
            response.setToken(jwt);
            response.setAccountNumber(user.getAccountNumber());
            response.setFullName(user.getFullName());
            response.setEmail(user.getEmail());
            
            return response;
        } catch (Exception e) {
            throw new BankException("Invalid email or password");
        }
    }
    
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BankException("User not found"));
    }
}