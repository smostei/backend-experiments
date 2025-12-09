package com.backend.test.backendtest.data.document

data class BanksResponse(
    val banks: Collection<Bank>
)

data class Bank(
    val accountNumber: String,
    val trust: Double,
    val transactionFee: Int,
)