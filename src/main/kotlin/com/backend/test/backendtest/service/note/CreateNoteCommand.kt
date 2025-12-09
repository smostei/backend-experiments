package com.backend.test.backendtest.service.note

data class CreateNoteCommand(
    val title: String,
    val content: String,
    val color: Long
)