package com.hyunec.app.api.service

import com.hyunec.app.api.domain.entity.ChatThread
import com.hyunec.app.api.persistence.repository.ChatThreadRepository
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class ChatCompletionStreamService(
    private val chatModel: OpenAiChatModel,

    private val chatThreadRepository: ChatThreadRepository
) {
    fun call(message: String, model: String): Flux<ChatResponse> {
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
    ): Flux<ChatResponse> {
        // chatMessage 생성
        // - chatData 의 생성 시간이 지금부터 30분 이내인 것만
        val chatMessages = chatThread.chatData
            .filter { ChronoUnit.MINUTES.between(it.createdAt, Instant.now()) <= 30 }
            .map { UserMessage("question" + ": " + it.question + "\n" + "answer" + ": " + it.answer) }
            .toMutableList().apply { add(UserMessage("question: $message")) }

        // open ai 호출
        val chatResponse = chatModel.stream(
            Prompt(
                chatMessages.toList(),
                OpenAiChatOptions.builder()
                    .withModel(model)
                    .build()
            )
        )

        // todo
        // 응답을 스레드에 추가
        // - 1 token 씩 오는 응답을 더해야 됨으로
        // - coroutine 이나 MDC 를 사용해서 데이터를 모아놓은 후
        // - 요청이 끝나면 한번에 저장한다.

        // 응답 반환
        return chatResponse
    }

    private fun chatThreadNotExists(
        message: String,
        model: String
    ): Flux<ChatResponse> {
        // open ai 호출
        val chatResponse = chatModel.stream(
            Prompt(
                message,
                OpenAiChatOptions.builder()
                    .withModel(model)
                    .build()
            )
        )

        // todo
        // 응답을 스레드에 추가

        // 응답 반환
        return chatResponse
    }

    private fun getUserid(): String {
        return SecurityContextHolder.getContext().authentication.name
    }
}
