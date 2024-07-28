package com.hyunec.app.api.controller.auth

import com.hyunec.app.api.config.security.JwtTokenProvider
import com.hyunec.app.api.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthSigninController(
    private val userService: UserService,

    private val jwtTokenProvider: JwtTokenProvider
) {
    @PostMapping("/api/v1/auth/signin")
    fun signup(@RequestBody request: Request): Response {
        val user = userService.signin(request.userId, request.password)
            ?: throw IllegalArgumentException("User not found")
        return Response(
            token = jwtTokenProvider.createToken(user)
        )
    }

    data class Request(
        val userId: String,
        val password: String,
    )

    data class Response(
        val token: String
    )
}
