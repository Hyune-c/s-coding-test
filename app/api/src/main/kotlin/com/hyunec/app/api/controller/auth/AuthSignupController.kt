package com.hyunec.app.api.controller.auth

import com.hyunec.app.api.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthSignupController(
    private val userService: UserService
) {
    @PostMapping("/api/v1/auth/signup")
    fun signup(@RequestBody request: Request) {
        userService.signup(request.email, request.password, request.name)
    }

    data class Request(
        val email: String,
        val password: String,
        val name: String,
    )
}
