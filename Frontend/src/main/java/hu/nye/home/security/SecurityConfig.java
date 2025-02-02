package hu.nye.home.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

//https://www.youtube.com/watch?v=c9qCrekFTG4&list=PL82C6-O4XrHe3sDCodw31GjXbwRdCyyuY&index=6

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private JwtAuthEntryPoint authEntryPoint;
    private CustomUserDetailsService userDetailsService;
    
    public SecurityConfig(JwtAuthEntryPoint authEntryPoint, CustomUserDetailsService userDetailsService) {
        this.authEntryPoint = authEntryPoint;
        this.userDetailsService = userDetailsService;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .csrf(AbstractHttpConfigurer::disable)
          .cors(cors -> cors.configurationSource(request -> {
              CorsConfiguration config = new CorsConfiguration();
              config.setAllowedOrigins(List.of("http://localhost:3000"));
              config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
              config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
              config.setAllowCredentials(true);
              return config;
          }))
          .exceptionHandling(exceptionHandling -> exceptionHandling
                                                    .accessDeniedHandler(accessDeniedHandler())
                                                    .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
          )
          .sessionManagement(sessionManagement -> sessionManagement
                                                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          )
          .authorizeHttpRequests(auth -> auth
                                           .requestMatchers("/api/auth/**").permitAll()
                                           .requestMatchers("/user-api/**").permitAll()
                                           .requestMatchers("/gd-api/**").permitAll()
                                           .requestMatchers("/api/images/**").permitAll()
                                           .requestMatchers("/comment-api/**").permitAll()
                                           .anyRequest().authenticated()
          )
          .httpBasic(httpBasic -> {});
        
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration
    ) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(403);
            response.getWriter().write("Hozzáférés megtagadva!");
        };
    }
    
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
}
