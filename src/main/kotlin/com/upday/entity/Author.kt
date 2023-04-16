package com.upday.entity

import jakarta.persistence.*

@Entity
data class Author(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String? = null,
)
