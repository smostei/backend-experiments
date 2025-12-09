package com.backend.test.backendtest.service.note

data class UpdateNoteCommand(
    val id: String,
    val title: String,
    val content: String,
    val color: Long
)