package com.backend.test.backendtest.database.repository

import com.backend.test.backendtest.database.model.Note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository: MongoRepository<Note, ObjectId> {

    fun findByOwnerId(ownerId: ObjectId): List<Note>
}