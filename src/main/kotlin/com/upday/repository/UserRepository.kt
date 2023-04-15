package com.upday.repository

import com.upday.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findByName(name: String): List<User>
    fun findByEmail(email: String): User?
    fun deleteByEmail(email: String)
}