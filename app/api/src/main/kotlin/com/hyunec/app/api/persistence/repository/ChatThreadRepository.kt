package com.hyunec.app.api.persistence.repository

import com.hyunec.app.api.domain.entity.ChatThread
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class ChatThreadRepository(
    private val pool: ConcurrentHashMap<String, ChatThread> = ConcurrentHashMap()
) {
    fun save(chatThread: ChatThread): String {
        pool[chatThread.id] = chatThread
        return chatThread.id
    }

    fun findById(id: String): ChatThread? {
        return pool[id]
    }

    fun findByUserId(userId: String): ChatThread? {
        return pool.filter { it.value.userId == userId }.values.firstOrNull()
    }

    fun findAll(): List<ChatThread> {
        return pool.values.toList()
    }
}
