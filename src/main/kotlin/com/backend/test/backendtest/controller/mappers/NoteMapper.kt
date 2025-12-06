package com.backend.test.backendtest.controller.mappers

import com.backend.test.backendtest.controller.NoteController.NoteResponse
import com.backend.test.backendtest.database.model.Note

fun Note.toResponse(): NoteResponse {
    return NoteResponse(
        id = this.id.toHexString(),
        title = this.title,
        content = this.content,
        color = this.color,
        createdAt = this.createdAt
    )
}