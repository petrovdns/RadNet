package com.petrovdns.radnet.web;

import com.petrovdns.radnet.payload.request.LoginRequest;
import com.petrovdns.radnet.payload.request.SignupRequest;
import com.petrovdns.radnet.payload.response.JWTokenSuccessResponse;
import com.petrovdns.radnet.payload.response.MessageResponse;
import com.petrovdns.radnet.security.JWTTokenProvider;
import com.petrovdns.radnet.security.SecurityConstants;
import com.petrovdns.radnet.service.UserService;
import com.petrovdns.radnet.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
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

    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ResponseErrorValidation responseErrorValidation;
    private final UserService userService;


    public AuthController(
            JWTTokenProvider jwtTokenProvider,
            AuthenticationManager authenticationManager,
            ResponseErrorValidation responseErrorValidation,
            UserService userService)
    {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.responseErrorValidation = responseErrorValidation;
        this.userService = userService;
    }

    @PostMapping("/signing")
    public ResponseEntity<Object> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest,
            BindingResult bindingResult)
    {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTokenSuccessResponse(true, jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(
            @Valid @RequestBody SignupRequest signupRequest,
            BindingResult bindingResult)
    {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registred successfully"));
    }
}
