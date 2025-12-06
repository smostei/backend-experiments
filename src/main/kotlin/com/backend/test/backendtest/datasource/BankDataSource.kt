package com.backend.test.backendtest.datasource

import com.backend.test.backendtest.database.model.Bank
import com.backend.test.backendtest.database.model.BanksResponse

interface BankDataSource {

    fun getBanks(): BanksResponse

    fun getBankByAccountNumber(accountNumber: String): Bank

    fun postBank(bank: Bank): Bank
}