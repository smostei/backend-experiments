package com.backend.test.backendtest.controller

import com.backend.test.backendtest.data.document.Bank
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class BankControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    companion object {
        private const val BASE_ENDPOINT = "/api/banks"
    }

    @Nested
    @DisplayName("GET /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks {

        @Test
        fun `should return all banks`() {
            // when
            // then
            mockMvc.get(BASE_ENDPOINT)
                .andDo { print() }
                .andExpect {
                    content { contentType(MediaType.APPLICATION_JSON) }
                    status { isOk() }
                }
        }
    }

    @Nested
    @DisplayName("GET /api/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBankByAccountNumber() {

        @Test
        fun `should return single bank by account number`() {
            // when
            // then
            val accountNumber = 1234

            mockMvc.get("$BASE_ENDPOINT/$accountNumber")
                .andDo { print() }
                .andExpect {
                    content { contentType(MediaType.APPLICATION_JSON) }
                    status { isOk() }
                }
        }

        @Test
        fun `should return NOT FOUND if account number does not exist`() {
            // when
            // then
            val accountNumber = "does_not_exist"

            mockMvc.get("$BASE_ENDPOINT/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostBank {

        @Test
        fun `should add a new bank`() {
            // given
            val newBank = Bank("9999", 31.45, 5)

            // when
            val performPost = mockMvc.post(BASE_ENDPOINT) {
                // Body request
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    content { contentType(MediaType.APPLICATION_JSON) }
                    status { isCreated() }
                    jsonPath("$.accountNumber") { value("9999") }
                    jsonPath("$.trust") { value("31.45") }
                    jsonPath("$.transactionFee") { value("5") }
                }
        }

        @Test
        fun `should return BAD REQUEST if bank with given account number already exists`() {
            // given
            val repeatedBank = Bank("repeated_account_number", 31.45, 5)

            // when
            mockMvc.post(BASE_ENDPOINT) {
                // Body request (first time will be created successfully)
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(repeatedBank)
            }

            val repeatedBankPost = mockMvc.post(BASE_ENDPOINT) {
                // Body request (this time will fail)
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(repeatedBank)
            }

            // then
            repeatedBankPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }
}