package com.backend.test.backendtest.controller

import com.backend.test.backendtest.controller.mappers.toResponse
import com.backend.test.backendtest.database.model.Note
import com.backend.test.backendtest.database.repository.NoteRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteController(
    private val repository: NoteRepository
) {

    // web or mobile send this to our backend
    data class NoteRequest(
        val id: String?,
        @field:NotBlank(message = "Title can not be blank")
        val title: String,
        val content: String,
        val color: Long,
    )

    // our backend send this to web or mobile
    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant,
    )

    // example: POST http://localhost:9000/notes
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(
        @Valid @RequestBody body: NoteRequest
    ): NoteResponse {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        val note = repository.save(
            Note(
                id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
                ownerId = ObjectId(ownerId),
                title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
            )
        )

        return note.toResponse()
    }

    // example: GET http://localhost:9000/notes
    @GetMapping
    fun findByOwnerId(): List<NoteResponse> {
        // We retrieve de user id that we've already saved in JwtFilter when we do any request
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        return repository.findByOwnerId(ObjectId(ownerId)).map { note ->
            note.toResponse()
        }
    }

    // example: DELETE http://localhost:9000/notes/{id}
    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: String) {
        val noteToDelete = repository.findById(ObjectId(id)).orElseThrow {
            IllegalArgumentException("The note to delete could not be found")
        }

        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        if (noteToDelete.ownerId.toHexString() == ownerId) {
            repository.deleteById(ObjectId(id))
        }
    }
}