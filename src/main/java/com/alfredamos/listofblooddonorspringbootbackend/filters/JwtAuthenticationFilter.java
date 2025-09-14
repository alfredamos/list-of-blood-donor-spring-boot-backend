package com.alfredamos.listofblooddonorspringbootbackend.filters;

import com.alfredamos.listofblooddonorspringbootbackend.services.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.alfredamos.listofblooddonorspringbootbackend.exceptions.UnAuthorizedException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)  {
        var cookies = request.getCookies(); //----> Get all cookies.
        var accessToken = mySpecificCookieValue(cookies); //----> Get access token

        var requestURI = request.getRequestURI(); //----> Get current uri.

        //----> Check token only for non-public routes.
        if(!publicRoutes().contains(requestURI)) {

            try {
                var jwt = jwtService.parseToken(accessToken);

                //----> Check for null and expired jwt object.
//                if (jwt == null || jwt.isExpired()) {
//                    filterChain.doFilter(request, response);
//                    return;
//                }

                var role = jwt.getUserRole(); //----> Get the role of the current user.
                var email = jwt.getUserEmail(); //----> Get the email of current user.

                //----> Authenticate the current user.
                var authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority(AuthParams.role + role))
                );

                //----> Set authentication details.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //----> Update security context info.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (IllegalArgumentException | JwtException ex) {
                System.out.println("Token is invalid!!!!! ex : " + ex.getMessage());
                throw new UnAuthorizedException("Invalid or expired token" + ex.getMessage());
            }
        }


        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }

    }

    private String mySpecificCookieValue(Cookie[] cookies){
        if(cookies == null) return "";

        return Stream.of(cookies)
                .filter(cookie -> cookie.getName().equals(AuthParams.accessToken))
                .map(Cookie::getValue)
                .findFirst().orElse(null);

    }

    private List<String> publicRoutes(){
        return Arrays.asList("/api/auth/login", "/api/auth/refresh", "/api/auth/signup");

    }

}