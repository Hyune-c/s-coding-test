package com.hyunec.app.api.controller.auth

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthSignupController {
    @PostMapping("/api/v1/auth/signup")
    fun signup(@RequestBody request: Request) {

    }

    data class Request(
        val email: String,
        val password: String,
        val name: String,
    )
}
