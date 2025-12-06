package com.backend.test.backendtest.database.repository

import com.backend.test.backendtest.database.model.RefreshToken
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository: MongoRepository<RefreshToken, ObjectId> {

    fun findByUserIdAndHashedToken(userId: ObjectId, token: String): RefreshToken?
    fun deleteByUserIdAndHashedToken(userId: ObjectId, token: String)
}