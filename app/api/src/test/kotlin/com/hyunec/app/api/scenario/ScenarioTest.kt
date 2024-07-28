package com.hyunec.app.api.scenario

import com.fasterxml.jackson.databind.ObjectMapper
import com.hyunec.app.api.BaseSupport
import com.hyunec.app.api.persistence.repository.ChatThreadRepository
import com.hyunec.app.api.persistence.repository.UserRepository
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@TestMethodOrder(MethodOrderer.DisplayName::class)
@AutoConfigureMockMvc
class ScenarioTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,

    private val userRepository: UserRepository,
    private val chatThreadRepository: ChatThreadRepository,
) : BaseSupport() {

    @ParameterizedTest
    @CsvSource(
        "testuser@example.com, pppassword, Test User",
    )
    fun `A1) 회원 가입`(email: String, password: String, name: String) {
        mockMvc.perform(
            post("/api/v1/auth/signup")
                .contentType("application/json")
                .content(
                    objectMapper.writeValueAsString(
                        mapOf(
                            "email" to email,
                            "password" to password,
                            "name" to name
                        )
                    )
                )
        )

        val user = userRepository.findByEmail(email)!!
        user.email shouldBe email
        user.name shouldBe name
    }

    @ParameterizedTest
    @CsvSource(
        "testuser@example.com, pppassword, Test User",
    )
    fun `A2) 회원 로그인`(email: String, password: String, name: String) {
        val response = mockMvc.perform(
            post("/api/v1/auth/signin")
                .contentType("application/json")
                .content(
                    objectMapper.writeValueAsString(
                        mapOf(
                            "userId" to email,
                            "password" to password,
                        )
                    )
                )
        ).andExpect(status().isOk)
            .andReturn()
            .run {
                this.response.characterEncoding = "UTF-8"
                this.response.contentAsString
            }

        log.debug("### result: $response")

        response.length shouldBeGreaterThan 0
    }

    @WithMockUser(username = "testuser@example.com", password = "pppassword", roles = ["USER"])
    @Test
    fun `E1) chat completion`() {
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

        val userId = "testuser@example.com"
        val chatThread = chatThreadRepository.findByUserId(userId)!!
        chatThread shouldNotBe null

        log.debug("### chatThread: $chatThread")
        chatThread.startMessageAt shouldBe chatThread.lastMessageAt
    }

    @WithMockUser(username = "testuser@example.com", password = "pppassword", roles = ["USER"])
    @Test
    fun `E2) chat completion - model 제외`() {
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

        val userId = "testuser@example.com"
        val chatThread = chatThreadRepository.findByUserId(userId)!!
        chatThread shouldNotBe null

        log.debug("### chatThread: $chatThread")
        chatThread.startMessageAt shouldNotBe chatThread.lastMessageAt
    }
}
