package com.spring.kkaemiGG.config.auth;

import com.google.gson.Gson;
import com.spring.kkaemiGG.web.dto.user.UsersLoginResponseDto;
import org.apache.commons.codec.CharEncoding;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String redirectUrl = "/";
        String prevPage = (String) request.getSession().getAttribute("prevPage");

        /*
        * 유저가 권한이 필요한 요청을 하면 시큐리티가 요청을 가로채게 되는데,
        * 유저가 원래 요청한 URL 정보를 RequestCache 안에 SavedRequest에 저장한다.
        *
        * RequestCache와 SavedRequest는 인터페이스이다.
        * 이를 구현한 기본 구현체로는 HttpSessionRequestCache와 DefaultSavedRequest가 있다.
        * */
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        // 유저가 권한이 필요한 요청을 했을 경우
        if (savedRequest != null) {
            redirectUrl = savedRequest.getRedirectUrl();

            // 세션에 저장된 객체를 다 사용한 뒤에는 지워줘서 메모리 누수 방지
            requestCache.removeRequest(request, response);

            System.out.println("intercepted!!! redirect url is " + redirectUrl);
        }

        // 유저가 로그인 버튼을 눌러서 로그인 할 경우
        if (StringUtils.hasText(prevPage)) {
            redirectUrl = prevPage;

            // 세션에 저장된 객체를 다 사용한 뒤에는 지워줘서 메모리 누수 방지
            request.getSession().removeAttribute("prevPage");

            System.out.println("normal request, redirect url is " + redirectUrl);
        }

        UsersLoginResponseDto responseDto = UsersLoginResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("LOGIN_SUCCEEDED")
                .redirectUrl(redirectUrl)
                .build();

        PrintWriter out = response.getWriter();
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setStatus(HttpStatus.OK.value());
        out.print(new Gson().toJson(responseDto));
        out.flush();

    }

}
