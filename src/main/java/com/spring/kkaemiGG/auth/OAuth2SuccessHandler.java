package com.spring.kkaemiGG.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.kkaemiGG.config.AppProperties;
import com.spring.kkaemiGG.domain.token.RefreshToken;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.exception.BadRequestException;
import com.spring.kkaemiGG.service.TokenService;
import com.spring.kkaemiGG.service.UserService;
import com.spring.kkaemiGG.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

import static com.spring.kkaemiGG.auth.CookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.spring.kkaemiGG.auth.Token.REFRESH_TOKEN_MAX_AGE;
import static com.spring.kkaemiGG.auth.Token.REFRESH_TOKEN_NAME;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final AppProperties appProperties;
    private final CookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userService.findByEmail(oAuth2User.getAttribute("email"));
        Token token = tokenService.generateToken(user);
        String redirectUrl = getRedirectUrl(request, token.getAccessToken());

        setRefreshToken(response, user, token.getRefreshToken());
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private void setRefreshToken(HttpServletResponse response, User user, String refreshToken) {
        Long id = tokenService.saveOrUpdate(user, refreshToken);
        CookieUtils.addCookie(response, REFRESH_TOKEN_NAME, id.toString(), REFRESH_TOKEN_MAX_AGE);
    }

    private String getRedirectUrl(
            HttpServletRequest request,
            String accessToken
    ) {
        String redirectUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElseGet(this::getDefaultTargetUrl);

        if (!isAuthorizedRedirectUri(redirectUrl)) {
            throw new BadRequestException("인증되지 않은 리다이렉트 URI 입니다.");
        }

        return UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("token", accessToken)
                .build().toUriString();
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOAuth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}
