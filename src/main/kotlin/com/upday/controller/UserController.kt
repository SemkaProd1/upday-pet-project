package com.upday.controller

import com.upday.service.UserService
import com.upday.entity.User

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @Operation(summary = "Create a new user")
    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<User> {
        val createdUser = userService.createUser(user)
        return ResponseEntity(createdUser, HttpStatus.CREATED)
    }

    @Operation(summary = "Get all users")
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        val users = userService.getAllUsers()
        return ResponseEntity(users, HttpStatus.OK)
    }

    @Operation(summary = "Get a user by ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User found"),
            ApiResponse(responseCode = "404", description = "User not found")
        ]
    )
    @GetMapping("/{id}")
    fun getUserById(
        @Parameter(description = "User ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<User> {
        val user = userService.getUserById(id)
        return if (user != null) {
            ResponseEntity(user.copy(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @Operation(summary = "Update a user")
    @PutMapping("/{id}")
    fun updateUser(
        @Parameter(description = "User ID", required = true)
        @PathVariable id: Long,
        @RequestBody user: User
    ): ResponseEntity<User> {
        if (user.id != id) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        val updatedUser = userService.updateUser(user)
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }

    @Operation(summary = "Delete a user by ID")
    @DeleteMapping("/{id}")
    fun deleteUserById(
        @Parameter(description = "User ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        userService.deleteUserById(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}
