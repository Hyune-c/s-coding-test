package com.hyunec.app.api.controller.auth

import org.apache.coyote.Response
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthSigninController {
    @PostMapping("/api/v1/auth/signin")
    fun signup(@RequestBody request: Request): Response {
        return Response(
            token = "dummy"
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
