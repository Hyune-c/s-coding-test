package com.hyunec.app.api.controller.chat

import com.hyunec.app.api.service.ChatCompletionReadService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class ChatCompletionReadAllController(
    private val chatCompletionReadService: ChatCompletionReadService
) {
    @GetMapping("/api/v1/chat/completion")
    fun readAll(): Response {
        return Response(
            chatData = chatCompletionReadService.readAll().map {
                Response.ChatData(
                    question = it.question,
                    answer = it.answer,
                    createdAt = it.createdAt
                )
            }
        )
    }

    data class Response(
        val chatData: List<ChatData>,

        // todo page 정보 추가
    ) {
        data class ChatData(
            val question: String,
            val answer: String,
            val createdAt: Instant
        )
    }
}
