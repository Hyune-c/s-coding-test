package com.hyunec.app.api.service

import com.hyunec.app.api.domain.entity.ChatThread
import com.hyunec.app.api.persistence.repository.ChatThreadRepository
import org.springframework.ai.chat.messages.UserMessage
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
    fun call(message: String, model: String): ChatResponse {
        // 기존 스레드 찾기
        val chatThread = chatThreadRepository.findByUserId(getUserid())

        return if (chatThread == null) {
            chatThreadNotExists(message, model)
        } else {
            chatTreanExsits(chatThread, message, model)
        }
    }

    private fun chatTreanExsits(
        chatThread: ChatThread,
        message: String,
        model: String
    ): ChatResponse {
        // chatMessage 생성
        val chatMessages = chatThread.chatData.map {
            UserMessage("question" + ": " + it.question + "\n" + "answer" + ": " + it.answer)
        }.toMutableList().apply { add(UserMessage("question: $message")) }

        // open ai 호출
        val chatResponse = chatModel.call(
            Prompt(
                chatMessages.toList(),
                OpenAiChatOptions.builder()
                    .withModel(model)
                    .build()
            )
        )

        // 응답을 스레드에 추가
        chatThread.apply {
            chatData.add(
                ChatThread.ChatData(
                    question = message,
                    answer = chatResponse.results[0].output.content
                )
            )
        }.run {
            chatThreadRepository.save(this)
        }

        // 응답 반환
        return chatResponse
    }

    private fun chatThreadNotExists(
        message: String,
        model: String
    ): ChatResponse {
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

    // todo
    private fun getUserid(): String {
        return "1"
    }
}
