package com.hyunec.app.api.controller.chat

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatThreadDeleteController {
    @DeleteMapping("/api/v1/chat/thread/{threadId}")
    fun delete(@PathVariable threadId: String) {
    }
}
