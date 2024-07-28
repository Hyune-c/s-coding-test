package com.hyunec.app.api.controller.chat

import com.hyunec.app.api.service.ChatCompletionService
import com.hyunec.common.util.KLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatCompletionCreateController(
    private val chatCompletionService: ChatCompletionService
) {
    @PostMapping("/api/v1/chat/completion")
    fun create(@RequestBody request: Request): Response {
        val chatResponse = chatCompletionService.call(
            request.message,
            request.model
        )

        log.debug("### chatResponse: {}", chatResponse)

        return Response(
            message = chatResponse.results[0].output.content
        )
    }

    data class Request(
        val message: String,
        val model: String?,
        val isStreaming: Boolean?
    )

    data class Response(
        val message: String
    )

    companion object : KLogging()
}
