package com.upday.service

import com.upday.entity.User
import com.upday.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun createUser(user: User): User {
        return userRepository.save(user)
    }

    fun getAllUsers(): List<User> {
        val userList = ArrayList<User>()
        userRepository.findAll().forEach { userList.add(it) }
        return userList
    }

    fun getUserById(id: Long): User? {
        return userRepository.findByIdOrNull(id)
    }

    fun updateUser(user: User): User {
        return userRepository.save(user)
    }

    fun deleteUserById(id: Long) {
        userRepository.deleteById(id)
    }

    fun getUsersByName(name: String): List<User> {
        return userRepository.findByName(name)
    }

    fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun deleteUserByEmail(email: String) {
        userRepository.deleteByEmail(email)
    }

}