package com.upday.entity

import jakarta.persistence.*


@Entity
data class Keyword(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String? = null,
)