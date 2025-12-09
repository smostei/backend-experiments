package com.backend.test.backendtest.controller.note


import com.backend.test.backendtest.mappers.toCreateCommand
import com.backend.test.backendtest.mappers.toResponse
import com.backend.test.backendtest.mappers.toUpdateCommand
import com.backend.test.backendtest.service.note.NoteService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notes")
class NoteController(
    private val service: NoteService
) {
    // example: POST http://localhost:9000/notes
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(
        @Valid @RequestBody body: NoteRequest
    ): NoteResponse {
        val ownerId = getOwnerId()
        val noteDomain = service.create(
            ownerId = ownerId,
            command = body.toCreateCommand()
        )

        return noteDomain.toResponse()
    }

    // example: PUT http://localhost:9000/notes
    @PutMapping
    fun update(
        @Valid @RequestBody body: NoteRequest
    ): NoteResponse {
        val ownerId = getOwnerId()
        val noteDomain = service.update(
            ownerId = ownerId,
            command = body.toUpdateCommand()
        )

        return noteDomain.toResponse()
    }

    // example: GET http://localhost:9000/notes
    @GetMapping
    fun findByOwnerId(): List<NoteResponse> {
        val ownerId = getOwnerId()

        return service.findByOwnerId(ownerId).map { noteDomain ->
            noteDomain.toResponse()
        }
    }

    // example: DELETE http://localhost:9000/notes/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: String) {
        val ownerId = getOwnerId()
        service.deleteById(id, ownerId)
    }

    // We retrieve de user id that we've already saved in JwtFilter when we do any request
    private fun getOwnerId(): String = SecurityContextHolder.getContext().authentication.principal as String

}