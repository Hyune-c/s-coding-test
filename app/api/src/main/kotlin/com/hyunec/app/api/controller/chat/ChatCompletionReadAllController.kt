package com.hyunec.app.api.controller.chat

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatCompletionReadAllController {
    @GetMapping("/api/v1/chat/completion")
    fun readAll(): Response {
        return Response(
            message = "dummy"
        )
    }

    data class Response(
        val message: String
    )
}
