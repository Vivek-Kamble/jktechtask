package jktech.task.auth.controller;

import jktech.task.auth.dto.LoginRequest;
import jktech.task.auth.dto.RegisterRequest;
import jktech.task.auth.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody @Valid LoginRequest request) {
        Authentication auth = authService.authenticate(request.getUsername(), request.getPassword());
        String token = jwtTokenProvider.generateToken(auth);
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }
}
