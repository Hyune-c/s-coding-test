package com.hyunec.app.api.scenario

import com.fasterxml.jackson.databind.ObjectMapper
import com.hyunec.app.api.BaseSupport
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
) : BaseSupport() {

    @Test
    fun `대화 시작`() {
        val response = mockMvc.perform(
            post("/api/v1/chat/completion")
                .contentType("application/json")
                .content(
                    objectMapper.writeValueAsString(
                        mapOf(
                            "message" to "한국에서 제일 높은 산 3개 이름만 말해줘",
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
    }
}
