package com.hyunec.app.api.service.chat

import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.stereotype.Service

@Service
class ChatCompletionService(
    private val chatModel: OpenAiChatModel
) {
    fun call(message: String, model: String? = "gpt-3.5-turbo"): ChatResponse {
        return chatModel.call(
            Prompt(
                message,
                OpenAiChatOptions.builder()
                    .withModel(model)
                    .build()
            )
        )
    }
}
