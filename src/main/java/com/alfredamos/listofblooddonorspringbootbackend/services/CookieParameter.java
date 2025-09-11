package com.alfredamos.listofblooddonorspringbootbackend.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CookieParameter {
    private String cookieName;
    private Jwt cookieValue;
    private int expiration;
    private String cookiePath;

}
