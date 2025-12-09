package com.backend.test.backendtest.data.mongo

import com.backend.test.backendtest.data.document.RefreshToken
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository: MongoRepository<RefreshToken, ObjectId> {

    fun findByUserIdAndHashedToken(userId: ObjectId, token: String): RefreshToken?
    fun deleteByUserIdAndHashedToken(userId: ObjectId, token: String)
}