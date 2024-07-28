package com.hyunec.app.api.controller.chat

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatCompletionCreateController {
    @PostMapping("/api/v1/chat/completion")
    fun create(@RequestBody request: Request): Response {
        return Response(
            message = "dummy"
        )
    }

    data class Request(
        val message: String,
        val model: String = "gpt-3.5.-turbo",
        val isStreaming: Boolean = false
    )

    data class Response(
        val message: String
    )
}
