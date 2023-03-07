package com.example.komunikator.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class SecurityConfiguration extends WebMvcConfigurerAdapter {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and().cors()
                .and().csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/","/add_friend","/conversation/**").hasRole("USER")
                .mvcMatchers("/register","/login").permitAll();
        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Autowired
    UserDetailsService userDetailsService;
}
