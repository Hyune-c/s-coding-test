package com.hyunec.app.api.persistence.entity

import com.hyunec.common.util.id.UlidGenerator
import java.time.Instant

data class ChatThread(
    val id: String = UlidGenerator.get(),
    val userId: String,

    val chatData: MutableList<ChatData> = mutableListOf(),
) {
    val startMessageAt: Instant = chatData.first().createdAt
    val lastMessageAt: Instant = chatData.last().createdAt

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
