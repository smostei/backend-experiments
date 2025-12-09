package com.backend.test.backendtest.service.note

import com.backend.test.backendtest.domain.model.NoteDomain
import com.backend.test.backendtest.domain.repository.NoteRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class NoteService(
    private val repository: NoteRepository
) {

    fun create(ownerId: String, command: CreateNoteCommand): NoteDomain {
        val note = NoteDomain(
            id = null,
            ownerId = ownerId,
            title = command.title,
            content = command.content,
            color = command.color,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        return repository.save(note)
    }

    fun update(ownerId: String, command: UpdateNoteCommand): NoteDomain {
        val retrievedCreatedDate = findById(command.id)!!.createdAt!!
        val note = NoteDomain(
            id = command.id,
            ownerId = ownerId,
            title = command.title,
            content = command.content,
            color = command.color,
            createdAt = retrievedCreatedDate,
            updatedAt = Instant.now()
        )

        return repository.save(note)
    }

    fun findByOwnerId(ownerId: String): List<NoteDomain> {
        return repository.findByOwner(ownerId)
    }

    fun findById(id: String): NoteDomain? {
        return repository.findById(id)
    }

    fun deleteById(id: String, ownerId: String) {
        val noteToDelete = findById(id) ?: throw IllegalArgumentException("The note to delete could not be found.")

        if (noteToDelete.ownerId == ownerId) {
            repository.deleteById(id)
        } else {
            throw IllegalArgumentException("Owner incorrect.")
        }
    }
}