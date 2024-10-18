package com.example.myproject.controller;

import com.example.myproject.dto.SigninRequestDTO;
import com.example.myproject.dto.SignupRequestDTO;
import com.example.myproject.exception.InvalidProviderException;
import com.example.myproject.exception.MissingFieldException;
import com.example.myproject.exception.NoTokenException;
import com.example.myproject.service.UserService;
import com.example.myproject.utils.JWTutils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("api/1.0/user")
@Validated
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    JWTutils jwTutils;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody SignupRequestDTO signupRequest) {
        log.info("registerUser: {}", signupRequest);
        Map<String, Object> response = userService.registerUser(signupRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> signinUser(@RequestBody SigninRequestDTO signinRequest) {
        Map<String, Object> response;
        log.info(signinRequest.getProvider());
        if ("native".equals(signinRequest.getProvider())) {
            SigninRequestDTO nativeSigninRequestDTO = new SigninRequestDTO(signinRequest.getProvider(), signinRequest.getEmail(), signinRequest.getPassword());
            response = handleNativeLogin(nativeSigninRequestDTO);
            return ResponseEntity.ok(response);
        } else if ("facebook".equals(signinRequest.getProvider())) {
            if (signinRequest.getToken() == null || signinRequest.getToken().isEmpty()) {
                throw new MissingFieldException("Token cannot be null or empty for Facebook login");
            }
            log.info("fbsigninUser: {}", signinRequest);
            SigninRequestDTO fbSigninRequestDTO = new SigninRequestDTO(signinRequest.getProvider(), signinRequest.getToken());
            response = handleFacebookLogin(fbSigninRequestDTO);
            return ResponseEntity.ok(response);
        } else {
            throw new InvalidProviderException("Invalid provider");
        }
    }

    public Map<String, Object> handleNativeLogin(@Valid @RequestBody SigninRequestDTO nativeSigninRequest) {
        Map<String, Object> response = userService.signInUser(nativeSigninRequest);
        return response;
    }

    public Map<String, Object> handleFacebookLogin(@Valid @RequestBody SigninRequestDTO fbSigninRequest) {
//        log.info("User Info: {}", fbSigninRequest);
        Map<String, Object> response = userService.fbSigninUser(fbSigninRequest);
//        log.info("User Info: {}", response);
        return response;
    }


    @GetMapping("/signin")
    public String signIn() {
        return "login";
    }


    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@RequestHeader(value = "Authorization", required = false) String token) {

        Map<String, Object> response = new HashMap<>();

        if (token == null || !token.startsWith("Bearer ")) {
            throw new NoTokenException("No token provided");
        }

        String jwtToken = token.substring(7); // Remove "Bearer " prefix

        Map<String, Object> userProfile = userService.getProfile(jwtToken);
        response.put("data", userProfile);

        return ResponseEntity.ok(response);
    }


}
