package com.upday.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain

@Configuration
class AuthorizationConfiguration(
    @Autowired private val customUserDetailsService: CustomUserDetailsService
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    @Bean
    fun configureHttp(http: HttpSecurity): SecurityFilterChain {
        http.headers().frameOptions().disable()
        http.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/v3/api-docs/**","/swagger-ui/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic()
        return http.build()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val encoder = passwordEncoder()
        http.httpBasic()
        customUserDetailsService.saveAdmin("admin", encoder.encode("password"))
        http.apply(AuthenticationConfiguration(customUserDetailsService, encoder))
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}
