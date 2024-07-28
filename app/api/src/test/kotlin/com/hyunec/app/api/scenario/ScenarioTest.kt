package com.hyunec.app.api.scenario

import com.fasterxml.jackson.databind.ObjectMapper
import com.hyunec.app.api.BaseSupport
import com.hyunec.app.api.persistence.repository.ChatThreadRepository
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@TestMethodOrder(MethodOrderer.DisplayName::class)
@AutoConfigureMockMvc
class ScenarioTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,

    private val chatThreadRepository: ChatThreadRepository
) : BaseSupport() {

    @Test
    fun `1) chat completion`() {
        val response = mockMvc.perform(
            post("/api/v1/chat/completion")
                .contentType("application/json")
                .content(
                    objectMapper.writeValueAsString(
                        mapOf(
                            "message" to "1 + 5 는?",
                            "model" to "gpt-3.5-turbo",
                        )
                    )
                )
        )
            .andExpect(status().isOk)
            .andReturn()
            .run {
                this.response.characterEncoding = "UTF-8"
                this.response.contentAsString
            }

        log.debug("### result: $response")

        response.length shouldBeGreaterThan 0

        val userId = "1"
        val chatThread = chatThreadRepository.findByUserId(userId)!!
        chatThread shouldNotBe null

        log.debug("### chatThread: $chatThread")
        chatThread.startMessageAt shouldBe chatThread.lastMessageAt
    }

    @Test
    fun `2) chat completion - model 제외`() {
        val response = mockMvc.perform(
            post("/api/v1/chat/completion")
                .contentType("application/json")
                .content(
                    objectMapper.writeValueAsString(
                        mapOf(
                            "message" to "방금 알려준 답에 +5 해줘",
                        )
                    )
                )
        )
            .andExpect(status().isOk)
            .andReturn()
            .run {
                this.response.characterEncoding = "UTF-8"
                this.response.contentAsString
            }

        log.debug("### result: $response")

        response.length shouldBeGreaterThan 0

        val userId = "1"
        val chatThread = chatThreadRepository.findByUserId(userId)!!
        chatThread shouldNotBe null

        log.debug("### chatThread: $chatThread")
        chatThread.startMessageAt shouldNotBe chatThread.lastMessageAt
    }
}
