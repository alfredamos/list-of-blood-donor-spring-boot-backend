package com.alfredamos.listofblooddonorspringbootbackend.controllers;

import com.alfredamos.listofblooddonorspringbootbackend.dto.*;
import com.alfredamos.listofblooddonorspringbootbackend.exceptions.AuthenticationException;
import com.alfredamos.listofblooddonorspringbootbackend.services.AuthParams;
import com.alfredamos.listofblooddonorspringbootbackend.services.AuthService;
import com.alfredamos.listofblooddonorspringbootbackend.utils.ResponseMessage;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseMessage> changePassword(@Valid @RequestBody ChangePassword changePassword) {
        var responseMessage = authService.changePassword(changePassword);

        return ResponseEntity.ok(responseMessage);
    }

    @PatchMapping("/edit-profile")
    public ResponseEntity<ResponseMessage> editProfile(@Valid @RequestBody EditProfile editProfile) {
        var responseMessage = authService.editProfile(editProfile);

        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        var result = authService.getCurrentUser();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Login login, HttpServletResponse response) throws AuthenticationException {

        var result = authService.getLoginAccess(login, response);

        return ResponseEntity.ok(result);
    }


    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = AuthParams.refreshToken) String refreshToken, HttpServletResponse response){
        var accessToken = this.authService.getRefreshToken(refreshToken, response); //----> Get access token.

        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> signup(@Valid @RequestBody Signup signup) {
        var result = authService.signUp(signup);

        return ResponseEntity.ok(result);
    }
}
