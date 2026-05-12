package com.myhomeledger.app.web;

import com.myhomeledger.app.security.config.AuthCookieNames;
import com.myhomeledger.app.security.config.JwtProperties;
import com.myhomeledger.app.security.dto.LoginRequest;
import com.myhomeledger.app.security.dto.LogoutRequest;
import com.myhomeledger.app.security.dto.SignupRequest;
import com.myhomeledger.app.security.dto.TokenResponse;
import com.myhomeledger.app.security.exception.DuplicatePhoneException;
import com.myhomeledger.app.security.exception.InvalidCredentialsException;
import com.myhomeledger.app.security.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @GetMapping("/")
    public String landing(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "index";
    }

    @PostMapping("/web/login")
    public String login(
            @Valid @ModelAttribute("loginRequest") LoginRequest loginRequest,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "index";
        }
        try {
            TokenResponse tokens = authService.login(loginRequest);
            WebAuthCookies.writeAuthCookies(response, tokens, jwtProperties, request.isSecure());
            return "redirect:/home";
        } catch (InvalidCredentialsException e) {
            model.addAttribute("error", e.getMessage());
            return "index";
        }
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "signup";
    }

    @PostMapping("/web/signup")
    public String signup(
            @Valid @ModelAttribute("signupRequest") SignupRequest signupRequest,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        try {
            TokenResponse tokens = authService.signup(signupRequest);
            WebAuthCookies.writeAuthCookies(response, tokens, jwtProperties, request.isSecure());
            return "redirect:/home";
        } catch (DuplicatePhoneException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @PostMapping("/web/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String refresh = readCookie(request, AuthCookieNames.REFRESH_TOKEN);
        if (refresh != null && !refresh.isBlank()) {
            try {
                LogoutRequest body = new LogoutRequest();
                body.setRefreshToken(refresh);
                authService.logout(body);
            } catch (RuntimeException ignored) {
                // Still clear cookies so the browser session ends.
            }
        }
        WebAuthCookies.clearAuthCookies(response, request.isSecure());
        return "redirect:/";
    }

    private static String readCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return null;
        }
        for (var c : request.getCookies()) {
            if (name.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }
}
