package com.backend.test.backendtest.database.repository

import com.backend.test.backendtest.database.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, ObjectId> {

    fun findByEmail(email: String): User?
}