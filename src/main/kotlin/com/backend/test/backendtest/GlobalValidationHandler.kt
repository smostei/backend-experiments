package com.backend.test.backendtest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalValidationHandler {

    // This handler will handle request body errors for any endpoint which could has a bad request for client
    // For example: in AuthRequest client can not put a blank password, or not email format, etc
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationError(e: MethodArgumentNotValidException): ResponseEntity<ErrorRequest> {
        val errors = e.bindingResult.allErrors.map {
            it.defaultMessage ?: "Invalid value"
        }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST.value())
            .body(
                ErrorRequest(
                    statusCode = HttpStatus.BAD_REQUEST.value(),
                    errors = errors
                )
            )
    }

    data class ErrorRequest(
        val statusCode: Int,
        val errors: Collection<String>
    )
}