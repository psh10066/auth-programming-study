package com.sp.fc.web.config;

import com.sp.fc.user.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class OnlinePaperSecurityConfig {

    private final UserSecurityService userSecurityService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userSecurityService);

        return daoAuthenticationProvider;
    }

    private RememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(
            "paper-site-remember-me",
            userSecurityService
        );
        rememberMeServices.setParameter("remember-me");
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setTokenValiditySeconds(3600);
        return rememberMeServices;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, @Autowired AuthenticationManager authenticationManager) throws Exception {
        final SpLoginFilter filter = new SpLoginFilter(authenticationManager, rememberMeServices());
        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(login -> login.loginPage("/login"))
            .logout(logout -> logout.logoutSuccessUrl("/"))
            .rememberMe(config -> config.rememberMeServices(rememberMeServices()))
            .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception ->
                exception
//                    .accessDeniedPage("/access-denied")
                    .authenticationEntryPoint(new CustomEntryPoint())
            )
            .authorizeHttpRequests(config -> config
                .requestMatchers(new AntPathRequestMatcher("/access-denied")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/schools")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/teachers")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/signup/*")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/signUp/*")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/student/**")).hasAnyAuthority("ROLE_ADMIN", "ROLE_STUDENT")
                .requestMatchers(new AntPathRequestMatcher("/teacher/**")).hasAnyAuthority("ROLE_ADMIN", "ROLE_TEACHER")
                .requestMatchers(new AntPathRequestMatcher("/manager/**")).hasAuthority("ROLE_ADMIN")
            );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
