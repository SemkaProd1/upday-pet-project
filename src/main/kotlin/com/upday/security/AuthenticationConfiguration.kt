package com.upday.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain

@Configuration
class AuthenticationConfiguration(
    @Autowired private val customUserDetailsService: CustomUserDetailsService,
    @Autowired private val passwordEncoder: PasswordEncoder
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(http: HttpSecurity) {
        http.authenticationProvider(daoAuthenticationProvider())
    }

    private fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val daoAuthProvider = DaoAuthenticationProvider()
        daoAuthProvider.setUserDetailsService(customUserDetailsService)
        daoAuthProvider.setPasswordEncoder(passwordEncoder)
        return daoAuthProvider
    }

}

