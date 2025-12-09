package com.backend.test.backendtest.controller.note

import jakarta.validation.constraints.NotBlank

// web or mobile send this to our backend
data class NoteRequest(
    val id: String?,
    @field:NotBlank(message = "Title can not be blank")
    val title: String,
    val content: String,
    val color: Long,
)