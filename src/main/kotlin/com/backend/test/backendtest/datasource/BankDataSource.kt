package com.backend.test.backendtest.datasource

import com.backend.test.backendtest.data.document.Bank
import com.backend.test.backendtest.data.document.BanksResponse

interface BankDataSource {

    fun getBanks(): BanksResponse

    fun getBankByAccountNumber(accountNumber: String): Bank

    fun postBank(bank: Bank): Bank
}