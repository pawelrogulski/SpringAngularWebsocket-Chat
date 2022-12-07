package com.example.komunikator.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/add_friend","/conversation/**").hasRole("USER")
                .mvcMatchers("/","/register","/login").permitAll()
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/");;
        return http.build();
    }
    @Autowired
    UserDetailsService userDetailsService;
}
