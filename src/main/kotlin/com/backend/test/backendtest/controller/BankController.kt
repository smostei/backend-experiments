package com.backend.test.backendtest.controller

import com.backend.test.backendtest.database.model.Bank
import com.backend.test.backendtest.database.model.BanksResponse
import com.backend.test.backendtest.service.BankService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/banks")
class BankController(
    private val service: BankService
) {
    @GetMapping
    fun getBanks(): BanksResponse = service.getBanks()

    @GetMapping("/{accountNumber}")
    fun getBankByAccountNumber(
        @PathVariable accountNumber: String
    ): Bank? = service.getBankByAccountNumber(accountNumber)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postBank(@RequestBody bank: Bank): Bank = service.postBank(bank)

    // Exceptions from endpoints handler
    @ExceptionHandler(
        NoSuchElementException::class,
        IllegalArgumentException::class
    )
    fun handleHttpExceptions(exception: Exception): ResponseEntity<String> {
        return ResponseEntity(
            exception.message,
            when (exception) {
                is NoSuchElementException -> HttpStatus.NOT_FOUND
                is IllegalArgumentException -> HttpStatus.BAD_REQUEST
                else -> HttpStatus.INTERNAL_SERVER_ERROR
            }
        )
    }
}