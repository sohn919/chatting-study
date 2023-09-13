package com.server.chatting.security;

import com.server.chatting.error.exception.Exception401;
import com.server.chatting.error.exception.Exception403;
import com.server.chatting.utils.FilterRespUtil;
import com.server.chatting.utils.JwtFilterRespUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtFilterRespUtil jwtFilterRespUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.cors().configurationSource(configurationSource());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.formLogin().disable(); // Spring Security가 기본적으로 제공하는 formLogin 기능을 사용하지 않겠다

        http.httpBasic().disable(); // 매 요청마다 id, pwd를 보내는 방식으로 인증하는 httpBasic를 사용하지 않겠다

        http.headers().frameOptions().sameOrigin(); // iframe 차단

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(jwtFilterRespUtil, JwtAuthenticationFilter.class);

        // 8. 인증 실패 처리
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            FilterRespUtil.unAuthorized(response, new Exception401("인증되지 않았습니다"));
        });

        // 9. 권한 실패 처리
        http.exceptionHandling().accessDeniedHandler((request, response, accessDeniedException) -> {
            FilterRespUtil.forbidden(response, new Exception403("권한이 없습니다"));
        });

        http.authorizeHttpRequests(auth ->
                auth.antMatchers("/**").permitAll());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
