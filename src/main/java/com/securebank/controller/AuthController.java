package com.securebank.controller;

import com.securebank.dto.AuthRequest;
import com.securebank.dto.AuthResponse;
import com.securebank.dto.TransactionRequest;
import com.securebank.model.Transaction;
import com.securebank.model.User;
import com.securebank.service.AccountService;
import com.securebank.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.authenticateUser(
                authRequest.getEmail(), 
                authRequest.getPassword());
        return ResponseEntity.ok(response);
    }
}