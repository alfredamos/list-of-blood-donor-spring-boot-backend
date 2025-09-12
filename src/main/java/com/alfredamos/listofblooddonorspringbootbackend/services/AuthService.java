package com.alfredamos.listofblooddonorspringbootbackend.services;

import com.alfredamos.listofblooddonorspringbootbackend.exceptions.*;
import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import com.alfredamos.listofblooddonorspringbootbackend.configs.JwtConfig;
import com.alfredamos.listofblooddonorspringbootbackend.dto.ChangePassword;
import com.alfredamos.listofblooddonorspringbootbackend.dto.EditProfile;
import com.alfredamos.listofblooddonorspringbootbackend.dto.Login;
import com.alfredamos.listofblooddonorspringbootbackend.dto.Signup;
import com.alfredamos.listofblooddonorspringbootbackend.dto.UserDto;
import com.alfredamos.listofblooddonorspringbootbackend.mapper.AuthMapper;
import com.alfredamos.listofblooddonorspringbootbackend.mapper.UserMapper;
import com.alfredamos.listofblooddonorspringbootbackend.repositories.UserRepository;
import com.alfredamos.listofblooddonorspringbootbackend.utils.ResponseMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;

    public ResponseMessage changePassword(ChangePassword changePasswordRequest){
        //----> Get the email, old-password, new-password, confirm-password
        var email = changePasswordRequest.getEmail();
        var newPassword = changePasswordRequest.getNewPassword();
        var oldPassword = changePasswordRequest.getOldPassword();
        var confirmPassword = changePasswordRequest.getConfirmPassword();

        //----> Check for password match.
        checkPasswordMatch(newPassword, confirmPassword);

        //----> Check for existence of user.
        var user = foundUserByEmail(email, AuthActionType.edit);

        //----> Check if the old password matches the one in the database.
        checkForCorrectPassword(oldPassword, user.getPassword());

        //----> Hash the new password.
        var hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);

        //----> Update the user in the database.
        userRepository.save(user);

        //----> Send back the response.
        return new ResponseMessage("Password has been changed successfully!", "Success", HttpStatus.OK);
    }

    public ResponseMessage editProfile(EditProfile editProfileRequest){
        //----> Get the email and password.
        var email = editProfileRequest.getEmail();
        var password = editProfileRequest.getPassword();

        //-----> Check for existence of user.
        var user = foundUserByEmail(email, AuthActionType.edit);

        //-----> Check if the giving password matches the one in the database.
        checkForCorrectPassword(password, user.getPassword());

        //----> Map edit-profile request to user
        editProfileRequest.setPassword(user.getPassword());
        var editedUser = authMapper.toEntity(editProfileRequest);

        //----> Save the edited profile
        editedUser.setId(user.getId());
        userRepository.save(editedUser);

        //----> Send back the response.
        return new ResponseMessage("User profile has been edited successfully!", "Success", HttpStatus.OK);
    }

    public ResponseMessage signUp(Signup signup){
        //----> Get email, password, confirm-password
        var email = signup.getEmail();
        var password = signup.getPassword();
        var confirmPassword = signup.getConfirmPassword();

        //----> Check for password match
        checkPasswordMatch(confirmPassword, password);

        //----> Check for existence of user.
        foundUserByEmail(email, AuthActionType.create);

        //----> Hash password.
        var hashedPassword = passwordEncoder.encode(password);
        signup.setPassword(hashedPassword); //----> Set the hashed password.

        //----> Map signup user to user.
        var user = authMapper.toEntity(signup);

        //----> Calculate the user's age.
        user.setAge(LocalDate.now().getYear() - user.getDateOfBirth().getYear());

        //----> save the new user in the database.
        userRepository.save(user);

        //----> Send back the response.
        return new ResponseMessage("Signup is successful!", "Success", HttpStatus.CREATED);
    }

    public ResponseMessage getLoginAccess(Login login, HttpServletResponse response) {
        //----> Authenticate user.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));

        //----> Get the authenticated user.
        var user = userRepository.findUserByEmail(login.getEmail());

        //----> Get access token.
        var accessToken = jwtService.generateAccessToken(user);

        //----> Put the access-token in the access-cookie.
        var accessCookie = makeCookie(new CookieParameter(AuthParams.accessToken, accessToken, (int)jwtConfig.getAccessTokenExpiration(), AuthParams.accessTokenPath
        ));

        //----> Add access-cookie to a response object.
        response.addCookie(accessCookie);

        //----> Get refresh-token
        var refreshToken = this.jwtService.generateRefreshToken(user);

        //----> Put the refresh-token in refresh-cookie.
        var refreshCookie = makeCookie(new CookieParameter(AuthParams.refreshToken, refreshToken, (int)this.jwtConfig.getRefreshTokenExpiration(), AuthParams.refreshTokenPath
        ));

        //----> Add refresh-cookie to a response object.
        response.addCookie(refreshCookie);

        return new ResponseMessage("Success", "Login is successful!", HttpStatus.OK);
    }

    public ResponseMessage removeLoginAccess(HttpServletResponse response){
        //----> Remove accessToken
        var accessCookie = makeCookie(new CookieParameter(AuthParams.accessToken, null, 0, AuthParams.accessTokenPath));

        //----> Add access-cookie to a response object.
        response.addCookie(accessCookie);


        //----> Remove refresh-cookie.
        var refreshCookie = makeCookie(new CookieParameter(AuthParams.refreshToken, null, 0, AuthParams.refreshTokenPath));

        //----> Add refresh-cookie to a response object.
        response.addCookie(refreshCookie);

        return new ResponseMessage("Success", "Login is successful!", HttpStatus.OK);

    }

    public UserDto getCurrentUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var email = (String) authentication.getPrincipal();
        var userDto = this.userMapper.toDTO(this.userRepository.findUserByEmail(email));

        if (userDto == null){
            throw  new NotFoundException("Current user is not found!");
        }

        return userDto;
    }

    public String getRefreshToken(String refreshToken, HttpServletResponse response){
        var jwt = jwtService.parseToken(refreshToken);

        if (jwt == null || jwt.isExpired()){
            throw new UnAuthorizedException("Invalid credentials!");
        }

        var user = this.userRepository.findById(jwt.getUserId()).orElseThrow();

        var accessToken = jwtService.generateAccessToken(user);

        //----> Put the access-token in the access-cookie.
        var accessCookie = makeCookie(new CookieParameter(AuthParams.accessToken, accessToken, (int)this.jwtConfig.getAccessTokenExpiration(), AuthParams.accessTokenPath
        ));

        response.addCookie(accessCookie);

        return  accessToken.toString();
    }



    private void checkPasswordMatch(String password, String confirmPassword){
        //----> Check for match between confirm-password and password.
        var isMatch = confirmPassword.equals(password);

        if (!isMatch){
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must match!");
            throw new BadRequestException("Password must match!");
        }
    }

    private void checkForCorrectPassword(CharSequence rawPassword, String storedPassword){
        var isCorrectPassword = this.passwordEncoder.matches(rawPassword, storedPassword);
        if (!isCorrectPassword){
            throw new UnAuthorizedException("Invalid credential!");
        }
    }

    private User foundUserByEmail(String email, String mode){
        var user = userRepository.findUserByEmail(email);
        if (mode.equalsIgnoreCase(AuthActionType.edit)) {
            if (user == null) {
                throw new NotFoundException("Invalid credential!");
            }
        } else if (mode.equalsIgnoreCase(AuthActionType.create)) {
            if (user != null) {
                throw new UnAuthorizedException("Invalid credential!");
            }
        }

        return user;
    }

    private Cookie makeCookie(CookieParameter  cookieParameter){
        //----> Set cookie.
        var cookie = new Cookie(cookieParameter.getCookieName(), cookieParameter.getCookieValue() == null ? null : cookieParameter.getCookieValue().toString());

        cookie.setHttpOnly(true);
        cookie.setPath(cookieParameter.getCookiePath());
        cookie.setMaxAge(cookieParameter.getExpiration());
        cookie.setSecure(false);

        return  cookie;
    }

}
