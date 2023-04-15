package com.upday.entity

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "User ID", example = "1")
    val id: Long = 0,

    @Schema(description = "User name", example = "John Doe")
    val name: String = "",

    @Schema(description = "User email", example = "john.doe@example.com")
    val email: String = ""
) {
    // Default constructor required by JPA
    constructor() : this(0, "", "")
}