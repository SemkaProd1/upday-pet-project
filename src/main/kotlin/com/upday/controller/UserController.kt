package com.upday.controller

import com.upday.entity.User
import com.upday.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SUPER')")
    fun createUser(@RequestBody user: User): ResponseEntity<User> {
        val createdUser = userService.createUser(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPER')")
    fun updateUser(@PathVariable id: Long, @RequestBody user: User): ResponseEntity<*> {
        userService.updateUser(user)
        return ResponseEntity.status(HttpStatus.OK).body("Updated")
    }
}
