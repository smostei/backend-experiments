package com.backend.test.backendtest.data.repository

import com.backend.test.backendtest.data.mongo.MongoNoteRepository
import com.backend.test.backendtest.domain.model.NoteDomain
import com.backend.test.backendtest.domain.repository.NoteRepository
import com.backend.test.backendtest.mappers.toDocument
import com.backend.test.backendtest.mappers.toDomain
import org.bson.types.ObjectId
import org.springframework.stereotype.Repository

@Repository
class NoteRepositoryImpl(
    private val mongo: MongoNoteRepository
) : NoteRepository {

    override fun save(note: NoteDomain): NoteDomain {
        val document = note.toDocument()
        val saved = mongo.save(document)

        return saved.toDomain()
    }

    override fun findByOwner(ownerId: String): List<NoteDomain> {
        return mongo.findByOwnerId(ObjectId(ownerId))
            .map { it.toDomain() }
    }

    override fun findById(id: String): NoteDomain? {
        return mongo.findById(ObjectId(id))
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun deleteById(id: String) {
        mongo.deleteById(ObjectId(id))
    }
}