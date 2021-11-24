package com.spring.kkaemiGG.util;

import com.spring.kkaemiGG.exception.BadRequestException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

public class CookieUtils {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            throw new BadRequestException("쿠키를 찾을 수 없습니다.");
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst();
    }

    public static void addCookie(
            HttpServletResponse response,
            String name,
            String value,
            int maxAge
    ) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    public static void deleteCookie(
            HttpServletRequest request,
            HttpServletResponse response,
            String cookieName
    ) {
        getCookie(request, cookieName)
                .ifPresent(cookie -> {
                    cookie.setValue(null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                });
    }
}
