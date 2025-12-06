package com.backend.test.backendtest.datasource

import com.backend.test.backendtest.datasource.implementation.BankDataSourceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BankDataSourceImplTest {

    private lateinit var bankDataSource: BankDataSource

    @BeforeEach
    fun setup() {
        bankDataSource = BankDataSourceImpl()
    }

    @Test
    fun `should provide a collection of banks`() {
        // given

        // when
        val banks = bankDataSource.getBanks().banks

        // then
        assertThat(banks).isNotEmpty
    }
}