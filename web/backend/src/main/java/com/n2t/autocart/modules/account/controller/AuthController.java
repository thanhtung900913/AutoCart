package com.n2t.autocart.modules.account.controller;

import com.n2t.autocart.modules.account.dto.LoginRequestDTO;
import com.n2t.autocart.modules.account.dto.LoginResponseDTO;
import com.n2t.autocart.modules.account.dto.UserCreateRequest;
import com.n2t.autocart.modules.account.dto.UserCreateResponse;
import com.n2t.autocart.modules.account.entity.User;
import com.n2t.autocart.modules.account.service.RefreshTokenService;
import com.n2t.autocart.modules.account.service.UserService;
import com.n2t.autocart.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final RefreshTokenService refreshTokenService;

    @Value("${auto-cart.jwt.refresh-token-validity-in-seconds}")
    private int refreshAxpiredTime;

    public AuthController(UserService userService, AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.refreshTokenService = refreshTokenService;
    }
    @PostMapping("/register")
    public ResponseEntity<UserCreateResponse> register(@RequestBody UserCreateRequest request){
        User user = userService.handleCreateUser(request);
        UserCreateResponse responseDTO = new UserCreateResponse(user.getId(), user.getEmail());
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.email(), loginRequestDTO.password()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
        LoginResponseDTO res = new LoginResponseDTO();
        User user = userService.getUserByEmail(loginRequestDTO.email());
        res.setUserResponse(new LoginResponseDTO.UserResponse(user.getId(),user.getEmail()));
        String accessToken = this.securityUtil.createAccessToken(authentication.getName(), res.getUserResponse());
        res.setAccessToken(accessToken);
        String refreshToken = this.securityUtil.createRefreshToken(loginRequestDTO.email(), res);
        refreshTokenService.createRefreshToken(refreshToken, user);
        ResponseCookie responseCookie = ResponseCookie.from("refreshTokenCookie", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshAxpiredTime)
                .build();;
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(res);
    }
    @GetMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(@CookieValue("refreshTokenCookie") String usernameCookie){
        Jwt token = securityUtil.verifyRefreshToken(usernameCookie);
        String email = token.getSubject();
        User user = userService.getUserByEmailAndToken(email, token.getTokenValue());
        LoginResponseDTO res = new LoginResponseDTO();
        res.setUserResponse(new LoginResponseDTO.UserResponse(user.getId(),user.getEmail()));
        String accessToken = this.securityUtil.createAccessToken(email, res.getUserResponse());
        res.setAccessToken(accessToken);
        String refreshToken = this.securityUtil.createRefreshToken(email, res);
        refreshTokenService.updateRefreshToken(refreshToken, usernameCookie);
        ResponseCookie responseCookie = ResponseCookie.from("refreshTokenCookie", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshAxpiredTime)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue("refreshTokenCookie") String usernameCookie){
//        Optional<String> currentUser = SecurityUtil.getCurrentUserLogin();
//        User user = userService.getUserByEmail(currentUser.get());
        refreshTokenService.updateRefreshToken(null, usernameCookie);
        ResponseCookie responseCookie = ResponseCookie.from("refreshTokenCookie", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(null);
    }
}
