package com.hyunec.app.api.domain.entity

import com.hyunec.common.util.id.UlidGenerator
import java.time.Instant

data class ChatThread(
    val id: String = UlidGenerator.get(),
    val userId: String,

    val chatData: MutableList<ChatData> = mutableListOf(),
) {
    val startMessageAt: Instant
        get() = chatData.first().createdAt
    val lastMessageAt: Instant
        get() = chatData.last().createdAt

    fun add(chatData: ChatData) {
        this.chatData.add(chatData)
    }

    data class ChatData(
        val id: String = UlidGenerator.get(),
        val question: String,
        val answer: String,

        val createdAt: Instant = Instant.now()
    )
}
