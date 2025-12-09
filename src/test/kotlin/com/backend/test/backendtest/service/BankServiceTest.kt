package com.backend.test.backendtest.service

import com.backend.test.backendtest.datasource.BankDataSource
import com.backend.test.backendtest.data.document.Bank
import com.backend.test.backendtest.data.document.BanksResponse
import com.backend.test.backendtest.service.bank.BankService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BankServiceTest {

    private lateinit var bankService: BankService

    private val dataSource = mockk<BankDataSource>()

    @BeforeEach
    fun setUp() {
        bankService = BankService(dataSource)
    }

    @Test
    fun `should call its data source to get banks`() {
        // given
        every { dataSource.getBanks() } returns BanksResponse(emptyList())

        // when
        bankService.getBanks()

        // then
        verify(exactly = 1) { dataSource.getBanks() }
    }

    @Test
    fun `should call its data source to get bank by account number`() {
        // given
        val accountNumber = "1234"
        every { dataSource.getBankByAccountNumber(accountNumber) } returns Bank(accountNumber, 1.0, 1)

        // when
        val bankResult = bankService.getBankByAccountNumber(accountNumber)

        // then
        verify(exactly = 1) { dataSource.getBankByAccountNumber(accountNumber) }
        assertEquals(accountNumber, bankResult?.accountNumber)
    }
}