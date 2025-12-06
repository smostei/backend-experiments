package com.backend.test.backendtest.datasource.implementation

import com.backend.test.backendtest.datasource.BankDataSource
import com.backend.test.backendtest.database.model.Bank
import com.backend.test.backendtest.database.model.BanksResponse
import org.springframework.stereotype.Repository

@Repository // With this annotation, we tell to spring that this class is able to put and retrieve data, etc
class BankDataSourceImpl : BankDataSource {

    private val banks = mutableListOf(
        Bank(
            accountNumber = "1234",
            trust = 3.14,
            transactionFee = 17,
        ),
        Bank(
            accountNumber = "1010",
            trust = 17.0,
            transactionFee = 0,
        ),
        Bank(
            accountNumber = "5678",
            trust = 0.0,
            transactionFee = 100,
        ),
    )

    override fun getBanks(): BanksResponse {
        return BanksResponse(banks)
    }

    override fun getBankByAccountNumber(accountNumber: String): Bank {
        return banks.firstOrNull { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")
    }

    override fun postBank(bank: Bank): Bank {
        if (banks.any { it.accountNumber == bank.accountNumber }) {
            throw IllegalArgumentException("Bank with account number ${bank.accountNumber} already exists")
        }

        banks.add(bank)
        return bank
    }
}