package com.iablonski.mynetwork.controller;

import com.iablonski.mynetwork.payload.request.LoginRequest;
import com.iablonski.mynetwork.payload.request.SignUpRequest;
import com.iablonski.mynetwork.payload.response.JWTTokenSuccessResponse;
import com.iablonski.mynetwork.payload.response.MessageResponse;
import com.iablonski.mynetwork.security.SecurityConstants;
import com.iablonski.mynetwork.security.jwt.JWTUtils;
import com.iablonski.mynetwork.service.UserService;
import com.iablonski.mynetwork.validation.ResponseErrorValid;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    private JWTUtils jwtUtils;
    private AuthenticationManager authenticationManager;
    private ResponseErrorValid responseErrorValid;
    private UserService userService;

    @Autowired
    public AuthController(JWTUtils jwtUtils,
                          AuthenticationManager authenticationManager,
                          ResponseErrorValid responseErrorValid,
                          UserService userService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.responseErrorValid = responseErrorValid;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                                   BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValid.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtUtils.generateToken(authentication);

        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }


    @PostMapping("/signup")
    public ResponseEntity<Object> userRegistration(@Valid @RequestBody SignUpRequest signupRequest,
                                                   BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValid.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
