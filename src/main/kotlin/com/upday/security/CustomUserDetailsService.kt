package com.upday.security

import com.upday.entity.User
import com.upday.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(@Autowired private val userRepository: UserRepository) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
                ?: throw UsernameNotFoundException("User not found with username: $username")
        return UserDetailsImpl(user)
    }

    fun saveAdmin(username: String, password: String): User {
        return userRepository.save(User(username = username, password = password, role = User.Role.SUPER))
    }
}