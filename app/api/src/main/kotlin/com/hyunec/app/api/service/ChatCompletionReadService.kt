package com.hyunec.app.api.service

import com.hyunec.app.api.domain.entity.ChatThread
import com.hyunec.app.api.persistence.repository.ChatThreadRepository
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class ChatCompletionReadService(
    private val chatModel: OpenAiChatModel,

    private val chatThreadRepository: ChatThreadRepository
) {
    fun readAll(): List<ChatThread.ChatData> {
        val chatThread = chatThreadRepository.findByUserId(getUserid())
        return chatThread?.chatData ?: emptyList()
    }

    private fun getUserid(): String {
        return SecurityContextHolder.getContext().authentication.name
    }
}
