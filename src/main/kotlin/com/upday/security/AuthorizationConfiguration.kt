package com.upday.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class AuthorizationConfiguration(
    @Autowired private val customUserDetailsService: CustomUserDetailsService,
    @Autowired private val delegatedAuthenticationEntryPoint: DelegatedAuthenticationEntryPoint,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.headers().frameOptions().disable()
            .and()
            .authenticationProvider(daoAuthenticationProvider())
            .csrf()
            .disable()
            .authorizeHttpRequests()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic().authenticationEntryPoint(delegatedAuthenticationEntryPoint)
        return http.build()
    }

    private fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val encoder = passwordEncoder()
        customUserDetailsService.saveAdmin("admin", encoder.encode("password"))

        val daoAuthProvider = DaoAuthenticationProvider()
        daoAuthProvider.setUserDetailsService(customUserDetailsService)
        daoAuthProvider.setPasswordEncoder(encoder)
        return daoAuthProvider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


}
