package com.backend.test.backendtest.service

import com.backend.test.backendtest.datasource.BankDataSource
import com.backend.test.backendtest.database.model.Bank
import com.backend.test.backendtest.database.model.BanksResponse
import org.springframework.stereotype.Service

@Service
class BankService(
    private val dataSource: BankDataSource
) {

    fun getBanks(): BanksResponse {
        return dataSource.getBanks()
    }

    fun getBankByAccountNumber(accountNumber: String): Bank {
        return dataSource.getBankByAccountNumber(accountNumber)
    }

    fun postBank(bank: Bank): Bank {
        return dataSource.postBank(bank)
    }
}