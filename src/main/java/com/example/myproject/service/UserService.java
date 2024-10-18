package com.example.myproject.service;

import com.example.myproject.dto.SigninRequestDTO;
import com.example.myproject.dto.SignupRequestDTO;
import com.example.myproject.dto.UserDTO;
import com.example.myproject.exception.*;
import com.example.myproject.generator.PasswordGenerator;
import com.example.myproject.repository.UserRepository;
import com.example.myproject.utils.JWTutils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JWTutils jwtUtils;
    @Autowired
    PasswordGenerator passwordGenerator;
    @Autowired
    CacheService cacheService;

    Map<String, Object> responseData;


    public Map<String, Object> registerUser(SignupRequestDTO signupRequest) {
        String token;
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        try {
            UserDTO user = new UserDTO();
            log.info("signupRequest: {}", signupRequest);
//            log.info(signupRequest.getProvider());
            if (signupRequest.getProvider() == null) {
                if (signupRequest.getPassword() == null || signupRequest.getEmail() == null || signupRequest.getName() == null) {
                    throw new MissingFieldException("Password, Email and Name must not be null");
                }
                user.setProvider("native");
                user.setPassword(signupRequest.getPassword());

            } else if (signupRequest.getProvider().equals("facebook")) {
                if (signupRequest.getEmail() == null || signupRequest.getName() == null) {
                    throw new MissingFieldException("Name and Email must not be null");
                }
                user.setProvider("facebook");
                user.setPassword(passwordGenerator.generateRandomPassword());
                user.setPicture(signupRequest.getPicture());
//                log.info(user.getPassword());
            } else {
                throw new InvalidProviderException("Invalid provider");
            }
            user.setName(signupRequest.getName());
            user.setEmail(signupRequest.getEmail());
            userRepository.save(user);
//            log.info(signupRequest.getProvider());
            try {
                token = jwtUtils.createJWT(user.getEmail(), user.getName());
//                log.info(token);
            } catch (Exception e) {
                throw new JWTException("Error creating JWT", e);
            }
            Map<String, Object> responseData = generateResponseData(token, jwtUtils.getExpireTime(), user.getId(), user.getProvider(), user.getName(), user.getEmail(), user.getPicture());
            return responseData;
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Error performing database operation");
        }
    }

    public Map<String, Object> signInUser(SigninRequestDTO signinRequest) {
        if (!userRepository.existsByEmail(signinRequest.getEmail())) {
            throw new EmailDoesntExistException("Email doesn't exist");
        }

        UserDTO user = new UserDTO();
        user.setProvider(signinRequest.getProvider());
        user.setEmail(signinRequest.getEmail());

        log.info("signInRequest: {}", signinRequest);

        try {
            if (user.getProvider().equals("native")) {
                user.setPassword(signinRequest.getPassword());
                userRepository.checkPassword(user); //check password and set user's name and id based on their email
                userRepository.updateUserProvider(user.getEmail(), user.getProvider()); //set provider base on their email
            } else if (user.getProvider().equals("facebook")) {
                user.setName(signinRequest.getName());
                userRepository.setUserId(user);
            }
            String token = jwtUtils.createJWT(user.getEmail(), user.getName());
            responseData = generateResponseData(token, jwtUtils.getExpireTime(), user.getId(), user.getProvider(), user.getName(), user.getEmail(), user.getPicture());
            return responseData;
        } catch (JWTException e) {
            throw new JWTException("Error creating JWT", e);
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Error performing database operation");
        }
    }

    public Map<String, Object> fbSigninUser(SigninRequestDTO signinRequest) {

        String token = signinRequest.getToken();
        String url = "https://graph.facebook.com/me?fields=id,name,email,picture&access_token=" + token;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> userInfo = response.getBody(); //get info from facebook

//            log.info("userInfo: {}", userInfo);
            String name = (String) userInfo.get("name"); //get name
            String email = (String) userInfo.get("email"); //get email
            String picture = getPictureUrl(userInfo); //get picture's url
            if (email == null) {
                throw new EmailDoesntExistException("Email doesn't exist"); //check if email exists
            } else {
                if (userRepository.existsByEmail(email)) { //email has already in SQL, so sign in

                    SigninRequestDTO fbSigninRequest = new SigninRequestDTO();
                    fbSigninRequest.setProvider("facebook");
                    fbSigninRequest.setName(name);
                    fbSigninRequest.setEmail(email);
                    fbSigninRequest.setPicture(picture);
//                    log.info("fbSigninRequest: {}", fbSigninRequest);

                    Map<String, Object> fbSigninResponse = signInUser(fbSigninRequest);
//                    log.info("fbSignupResponse: {}", fbSignupResponse);
                    return fbSigninResponse;

                } else { //email hasn't exist, so sign up

                    SignupRequestDTO fbSignupRequest = new SignupRequestDTO();
                    fbSignupRequest.setName(name);
                    fbSignupRequest.setEmail(email);
                    fbSignupRequest.setPicture(picture);
                    fbSignupRequest.setProvider("facebook");
//                    log.info("fbSignupRequest: {}", fbSignupRequest);
                    Map<String, Object> fbSignupResponse = registerUser(fbSignupRequest);
//                    log.info("fbSignupResponse: {}", fbSignupResponse);
                    return fbSignupResponse;
                }
            }
        } else {
            throw new RuntimeException("Failed to fetch user info from Facebook");
        }
    }

    public String getPictureUrl(Map<String, Object> userInfo) {
        String pictureUrl = "";
        Map<String, Object> picture = (Map<String, Object>) userInfo.get("picture");
        if (picture != null) {
            Map<String, Object> data = (Map<String, Object>) picture.get("data");
            if (data != null) {
                pictureUrl = (String) data.get("url"); // get picture's url
//                log.info("Picture URL: {}", pictureUrl);
            }
        }
        return pictureUrl;
    }


    public Map<String, Object> getProfile(String token) {
        try {
            Claims claims = jwtUtils.parseJWT(token);
            log.info("claims: {}", claims);

            String cacheKey = "profile:" + claims.getSubject();

            Map<String, Object> cachedData = cacheService.getProfileCacheData(cacheKey);

            if (cachedData != null) {
                log.info("Cache hit for key: {}", cacheKey);
                return cachedData;
            }

            Map<String, Object> data = userRepository.getProfile(claims.getSubject());

            Date expiration = claims.getExpiration();
            long timeout = expiration.toInstant().getEpochSecond() - Instant.now().getEpochSecond();

            if (timeout > 0) {
                cacheService.setProfileCacheData(cacheKey, data, timeout, TimeUnit.SECONDS);
                log.info("Cache miss. Data fetched from DB and cached with key: {}", cacheKey);
            } else {
                log.warn("JWT is already expired. No caching will be performed.");
            }

            return data;
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException("Invalid token");
        }
    }


    private Map<String, Object> generateResponseData(String token, long expireTime, long userId, String userProvider, String userName, String userEmail, String userPicture) {
        Map<String, Object> responseData = new HashMap<>();
        Map<String, Object> tokenData = new HashMap<>();
        Map<String, Object> userData = new HashMap<>();

        tokenData.put("access_token", token);
        tokenData.put("access_expired", expireTime / 1000);

        userData.put("id", userId);
        userData.put("provider", userProvider);
        userData.put("name", userName);
        userData.put("email", userEmail);
        userData.put("picture", (userPicture != null && !userPicture.isEmpty()) ? userPicture : "null");

        tokenData.put("user", userData);
        responseData.put("data", tokenData);

        return responseData;
    }
}

