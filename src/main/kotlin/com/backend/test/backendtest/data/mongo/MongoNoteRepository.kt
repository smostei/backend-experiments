package com.backend.test.backendtest.data.mongo

import com.backend.test.backendtest.data.document.Note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface MongoNoteRepository: MongoRepository<Note, ObjectId> {

    fun findByOwnerId(ownerId: ObjectId): List<Note>
}