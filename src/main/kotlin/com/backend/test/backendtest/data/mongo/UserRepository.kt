package com.backend.test.backendtest.data.mongo

import com.backend.test.backendtest.data.document.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, ObjectId> {

    fun findByEmail(email: String): User?
}