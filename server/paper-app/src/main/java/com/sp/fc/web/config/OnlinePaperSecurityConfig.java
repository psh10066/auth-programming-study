package com.sp.fc.web.config;

import com.sp.fc.user.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
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

    // 해당 버전에서는 따로 설정하지 않아도 Bean 설정에 따라 UserDetailsService, passwordEncoder 다 잘 불러옴 (UsernamePasswordAuthenticationFilter에서 확인)
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    private RememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(
            "paper-site-remember-me",
            userSecurityService
        );
        rememberMeServices.setParameter("remember-me");
//        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setTokenValiditySeconds(3600);
        return rememberMeServices;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(login ->
                login
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(new LoginSuccessHandler())
                    .failureHandler(new LoginFailureHandler())
            )
            .logout(logout -> logout.logoutSuccessUrl("/"))
            .rememberMe(config -> config.rememberMeServices(rememberMeServices()))
            .exceptionHandling(exception ->
                exception
                    .accessDeniedPage("/access-denied")
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
        return (web) ->
            web
                .ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .and()
                .ignoring().requestMatchers(PathRequest.toH2Console());
    }
}
