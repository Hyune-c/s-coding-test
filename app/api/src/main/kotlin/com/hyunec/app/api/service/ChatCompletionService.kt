package com.hyunec.app.api.service

import com.hyunec.app.api.persistence.entity.ChatThread
import com.hyunec.app.api.persistence.repository.ChatThreadRepository
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.stereotype.Service

@Service
class ChatCompletionService(
    private val chatModel: OpenAiChatModel,

    private val chatThreadRepository: ChatThreadRepository
) {
    fun call(message: String, model: String? = "gpt-3.5-turbo"): ChatResponse {
        // 기존 스레드 찾기

        // 있으면 open ai 호출시 같이 전달

        // open ai 호출
        val chatResponse = chatModel.call(
            Prompt(
                message,
                OpenAiChatOptions.builder()
                    .withModel(model)
                    .build()
            )
        )

        // 응답을 스레드에 추가
        ChatThread(
            userId = getUserid(),
            chatData = mutableListOf(
                ChatThread.ChatData(
                    question = message,
                    answer = chatResponse.results[0].output.content
                )
            )
        ).run {
            chatThreadRepository.save(this)
        }

        // 응답 반환
        return chatResponse
    }

    // tood
    private fun getUserid(): String {
        return "1"
    }
}
