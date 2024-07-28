package com.hyunec.app.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.hyunec.app.api.BaseSupport
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@TestMethodOrder(MethodOrderer.DisplayName::class)
@AutoConfigureMockMvc
class IndexControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) : BaseSupport() {

    @Test
    fun `1) health`() {
        val response = mockMvc.perform(
            get("/health")
        )
            .andExpect(status().isOk)
            .andReturn()
            .run {
                this.response.contentAsString
            }
        log.debug("### result: $response")
    }
}
