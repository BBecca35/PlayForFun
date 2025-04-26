package hu.nye.home.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static hu.nye.home.authorization.Permission.*;
import static hu.nye.home.authorization.Role.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthEntryPoint authEntryPoint;
    private final CustomUserDetailsService userDetailsService;
    private final LogoutHandler logoutHandler;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .csrf(AbstractHttpConfigurer::disable)
          .cors(cors -> cors.configurationSource(request -> {
              CorsConfiguration config = new CorsConfiguration();
              config.setAllowedOrigins(List.of("http://localhost:3000"));
              config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
              config.setAllowedHeaders(List.of("*"));
              config.setExposedHeaders(List.of("Authorization"));
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
                                           .requestMatchers("/user-api/register").permitAll()
                                           .requestMatchers("/api/images/**").permitAll()
                                           
                                           .requestMatchers("/user-api/user/**").hasAnyRole(ADMIN.name(), USER.name(), MODERATOR.name())
                                           .requestMatchers(HttpMethod.GET, "/user-api/user/get/**").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name(), MODERATOR_READ.name())
                                           .requestMatchers(HttpMethod.PUT, "/user-api/user/changeEmail", "/user-api/user/changePassword")
                                           .hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name(), MODERATOR_UPDATE.name())
                                           .requestMatchers(HttpMethod.DELETE, "/user-api/user/delete/**").hasAuthority(ADMIN_DELETE.name())
                                           
                                           .requestMatchers("/moderate-api/**").hasAnyRole(ADMIN.name(), MODERATOR.name())
                                           
                                           .requestMatchers("/gd-api/**").hasAnyRole(ADMIN.name(), USER.name(), MODERATOR.name())
                                           .requestMatchers(HttpMethod.POST, "/gd-api/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_CREATE.name(), MODERATOR_CREATE.name())
                                           .requestMatchers(HttpMethod.GET, "/gd-api/**").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name(), MODERATOR_READ.name())
                                           .requestMatchers(HttpMethod.PUT, "/gd-api/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name(), MODERATOR_UPDATE.name())
                                           .requestMatchers(HttpMethod.DELETE, "/gd-api/**").hasAnyAuthority(ADMIN_DELETE.name(), USER_DELETE.name(), MODERATOR_DELETE.name())
                                           
                                           .requestMatchers("/comment-api/**").hasAnyRole(ADMIN.name(), USER.name(), MODERATOR.name())
                                           .requestMatchers(HttpMethod.POST, "/comment-api/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_CREATE.name(), MODERATOR_CREATE.name())
                                           .requestMatchers(HttpMethod.GET, "/comment-api/**").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name(), MODERATOR_READ.name())
                                           .anyRequest().authenticated()
          )
          .httpBasic(httpBasic -> {})
          .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
          .logout(logout -> logout
                              .logoutUrl("/api/auth/logout")
                              .addLogoutHandler(logoutHandler)
                              .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
          );
        
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
