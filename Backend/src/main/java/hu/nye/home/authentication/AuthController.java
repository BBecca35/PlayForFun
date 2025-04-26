package hu.nye.home.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    public void login(@RequestBody AuthRequestDto dto, HttpServletResponse response){
        authService.authenticate(dto, response);
    }
    
    
    @PostMapping("/refresh")
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
        authService.refreshToken(request, response);
    }
}
