package com.backend.test.backendtest.mappers

import com.backend.test.backendtest.controller.note.NoteRequest
import com.backend.test.backendtest.controller.note.NoteResponse
import com.backend.test.backendtest.data.document.Note
import com.backend.test.backendtest.domain.model.NoteDomain
import com.backend.test.backendtest.service.note.CreateNoteCommand
import com.backend.test.backendtest.service.note.UpdateNoteCommand
import org.bson.types.ObjectId

fun NoteRequest.toCreateCommand(): CreateNoteCommand {
    return CreateNoteCommand(
        title = this.title,
        content = this.content,
        color = this.color
    )
}

fun NoteRequest.toUpdateCommand(): UpdateNoteCommand {
    return UpdateNoteCommand(
        id = this.id!!,
        title = this.title,
        content = this.content,
        color = this.color
    )
}

fun NoteDomain.toResponse(): NoteResponse {
    return NoteResponse(
        id = this.id!!,
        title = this.title,
        content = this.content,
        color = this.color,
        createdAt = this.createdAt!!,
        updatedAt = this.updatedAt
    )
}

fun NoteDomain.toDocument(): Note {
    return Note(
        id = this.id?.let { ObjectId(it) } ?: ObjectId.get(),
        ownerId = ObjectId(this.ownerId),
        title = this.title,
        content = this.content,
        color = this.color,
        createdAt = this.createdAt!!,
        updatedAt = this.updatedAt
    )
}

fun Note.toDomain(): NoteDomain {
    return NoteDomain(
        id = this.id.toHexString(),
        ownerId = this.ownerId.toHexString(),
        title = this.title,
        content = this.content,
        color = this.color,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}