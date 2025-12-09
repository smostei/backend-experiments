package com.backend.test.backendtest.controller.note

import java.time.Instant

// our backend send this to web or mobile
data class NoteResponse(
    val id: String,
    val title: String,
    val content: String,
    val color: Long,
    val createdAt: Instant,
    val updatedAt: Instant
)