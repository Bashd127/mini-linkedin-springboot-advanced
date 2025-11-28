package com.example.minilinkedadvanced.controller;

import com.example.minilinkedadvanced.dto.AuthRequest;
import com.example.minilinkedadvanced.dto.AuthResponse;
import com.example.minilinkedadvanced.entity.UserAccount;
import com.example.minilinkedadvanced.repository.UserRepository;
import com.example.minilinkedadvanced.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) {
        if (userRepository.findByEmail(req.email).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        UserAccount user = new UserAccount();
        user.setEmail(req.email);
        user.setPassword(passwordEncoder.encode(req.password));
        user.setFullName("New User");
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.email, req.password));
        Optional<UserAccount> opt = userRepository.findByEmail(req.email);
        if (opt.isEmpty()) return ResponseEntity.badRequest().body("Invalid credentials");
        UserAccount user = opt.get();
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user.getId()));
    }
}
