package com.upday.entity

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "User ID", example = "1")
    val id: Long = 0,

    @Schema(description = "User name", example = "John Doe")
    val username: String = "",

    @Schema(description = "encrypted password", example = "Fl4v#5l")
    var password: String = "",

    @Schema(description = "User Role", example = "Admin")
    val role: Role = Role.USER
) {
    // Default constructor required by JPA
    constructor() : this(0, "", "", Role.USER)

    enum class Role {
        SUPER, ADMIN, USER
    }
}