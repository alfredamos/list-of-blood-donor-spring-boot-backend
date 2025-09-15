package com.alfredamos.listofblooddonorspringbootbackend.services;

import com.alfredamos.listofblooddonorspringbootbackend.entities.Token;
import com.alfredamos.listofblooddonorspringbootbackend.entities.TokenType;
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
import com.alfredamos.listofblooddonorspringbootbackend.repositories.AuthRepository;
import com.alfredamos.listofblooddonorspringbootbackend.repositories.TokenRepository;
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

@Service
@RequiredArgsConstructor
public class AuthService{
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final TokenRepository tokenRepository;

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
        authRepository.save(user);

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
        authRepository.save(editedUser);

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
        authRepository.save(user);

        //----> Send back the response.
        return new ResponseMessage("Signup is successful!", "Success", HttpStatus.CREATED);
    }

    public ResponseMessage getLoginAccess(Login login, HttpServletResponse response) {
        //----> Authenticate user.
        var loginAction = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());

        //----> Authenticate user.
        authenticationManager.authenticate(loginAction);

        //----> Get the authenticated user.
        var user = authRepository.findUserByEmail(login.getEmail());

        //----> Revoke the previous access-token before getting a new one.
        revokedAllUserTokens(user);

        //----> Initialize a token
        var token = new Token();
        token.setUser(user);

        //----> Get access token.
        var accessToken = jwtService.generateAccessToken(user);
        token.setAccessToken(accessToken.toString());

        //----> Put the access-token in the access-cookie.
        var accessCookie = makeCookie(new CookieParameter(AuthParams.accessToken, accessToken, (int)jwtConfig.getAccessTokenExpiration(), AuthParams.accessTokenPath
        ));

        //----> Add access-cookie to a response object.
        response.addCookie(accessCookie);

        //----> Get refresh-token
        var refreshToken = this.jwtService.generateRefreshToken(user);
        token.setRefreshToken(refreshToken.toString());

        token.setTokenType(TokenType.Bearer);
        token.setExpired(false);
        token.setRevoked(false);

        //----> save the new token in the database.
        tokenRepository.save(token);


        //----> Put the refresh-token in refresh-cookie.
        var refreshCookie = makeCookie(new CookieParameter(AuthParams.refreshToken, refreshToken, (int)this.jwtConfig.getRefreshTokenExpiration(), AuthParams.refreshTokenPath
        ));

        //----> Add refresh-cookie to a response object.
        response.addCookie(refreshCookie);

        return new ResponseMessage("Success", "Login is successful!", HttpStatus.OK);
    }

    public UserDto getCurrentUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var email = (String) authentication.getPrincipal();
        var userDto = this.userMapper.toDTO(this.authRepository.findUserByEmail(email));

        if (userDto == null){
            throw  new UnAuthorizedException("Invalid credential!");
        }

        return userDto;
    }


    public String getRefreshToken(String refreshToken, HttpServletResponse response){
        //----> Parse the refresh-token.
        var jwt = jwtService.parseToken(refreshToken);

        if (jwt.isExpired()){
            throw new UnAuthorizedException("Invalid credentials!");
        }

        //----> Get the user-email.
        var email = jwt.getUserEmail();

        //----> Get the current-user.
        var user = authRepository.findUserByEmail(email);

        //----> Get the first valid token.
        var validToken = tokenRepository.findAllValidTokensByUser(user.getId()).getFirst();

        //----> Check for valid token.
        if (validToken.isRevoked() && validToken.isExpired()){
            throw new UnAuthorizedException("Invalid token!");
        }

        //----> Revoke the previous token before getting a new one.
        revokedAllUserTokens(user);

        //----> Create new token.
        var newToken = new Token();

        //----> Set user on the new token.
        newToken.setUser(user);

        //----> Get new access-token.
        var accessToken = jwtService.generateAccessToken(user);

        //----> Set access-token on token object.
        newToken.setAccessToken(accessToken.toString());

        //----> Get new refresh-token.
        var newRefreshToken = jwtService.generateRefreshToken(user);

        //----> Set refresh-token on token object.
        newToken.setRefreshToken(newRefreshToken.toString());

        //----> Set the token-type and set expired and revoked to false.
        newToken.setTokenType(TokenType.Bearer);
        newToken.setExpired(false);
        newToken.setRevoked(false);

        //----> save the edited token object in the database.
        tokenRepository.save(newToken);

        //----> Put the access-token in the access-cookie.
        var accessCookie = makeCookie(new CookieParameter(AuthParams.accessToken, accessToken, (int)this.jwtConfig.getAccessTokenExpiration(), AuthParams.accessTokenPath
        ));

        response.addCookie(accessCookie);//----> Put the access-token in the access-cookie.

        var refreshCookie = makeCookie(new CookieParameter(AuthParams.refreshToken, newRefreshToken, (int)this.jwtConfig.getRefreshTokenExpiration(), AuthParams.refreshTokenPath
        ));

        response.addCookie(refreshCookie);

        //----> Send back the new access-token.
        return  accessToken.toString();
    }

    public User getUserFromContext(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var email = (String) authentication.getPrincipal();
        var user = authRepository.findUserByEmail(email);

        if (user == null){
            throw  new UnAuthorizedException("Invalid credential!");
        }

        return user;
    }

    public void revokedAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);


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
        var user = authRepository.findUserByEmail(email);
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
