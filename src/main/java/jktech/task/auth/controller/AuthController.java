package jktech.task.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jktech.task.auth.dto.JwtAuthResponse;
import jktech.task.auth.dto.LoginRequest;
import jktech.task.auth.dto.RegisterRequest;
import jktech.task.auth.security.JwtTokenProvider;
import jktech.task.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Register a new user", description = "Creates a new user with specified role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Authenticates user and returns a JWT token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully authenticated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtAuthResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid username or password",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<JwtAuthResponse> login(@RequestBody @Valid LoginRequest request) {
        Authentication auth = authService.authenticate(request.getUsername(), request.getPassword());
        String token = jwtTokenProvider.generateToken(auth);
        return ResponseEntity.ok(new JwtAuthResponse(token,"Bearer"));
    }

    @GetMapping("/validate")
    @Operation(
            summary = "Validate JWT Token",
            description = "Checks whether a given JWT token is valid or not",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token is valid"),
                    @ApiResponse(responseCode = "400", description = "Invalid token")
            }
    )
    public ResponseEntity<String> validateToken(
            @RequestHeader("Authorization") String tokenHeader) {
        log.info("validateToken");
        try {
            String token = tokenHeader.replace("Bearer ", "");
            boolean valid = jwtTokenProvider.validateToken(token);
            if (valid) {
                return ResponseEntity.ok("Token is valid");
            } else {
                return ResponseEntity.badRequest().body("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid token: " + e.getMessage());
        }
    }
}
