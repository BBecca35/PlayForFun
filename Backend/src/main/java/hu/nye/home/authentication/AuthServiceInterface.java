package hu.nye.home.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthServiceInterface {
    void authenticate(AuthRequestDto dto, HttpServletResponse response);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
