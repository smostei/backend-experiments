package com.backend.test.backendtest.service

import com.backend.test.backendtest.database.model.RefreshToken
import com.backend.test.backendtest.database.model.User
import com.backend.test.backendtest.database.repository.RefreshTokenRepository
import com.backend.test.backendtest.database.repository.UserRepository
import com.backend.test.backendtest.security.HashEncoder
import com.backend.test.backendtest.security.JwtService
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val jwtService: JwtService,
    private val hashEncoder: HashEncoder,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    fun register(email: String, password: String): User {
        val user = userRepository.findByEmail(email.trim())

        if (user != null) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "The mail $email has already used.")
        }

        return userRepository.save(
            User(
                email = email,
                hashedPassword = hashEncoder.encode(password)
            )
        )
    }

    fun login(email: String, password: String): TokenPair {
        val user = userRepository.findByEmail(email)
            ?: throw BadCredentialsException("The email $email is invalid.")

        if (!hashEncoder.matches(password, user.hashedPassword)) {
            throw BadCredentialsException("Invalid credentials.")
        }

        val accessToken = jwtService.generateAccessToken(user.id.toHexString())
        val refreshToken = jwtService.generateRefreshToken(user.id.toHexString())

        val newAccessToken = TokenPair(
            accessToken = accessToken,
            refreshToken = refreshToken
        )

        storeRefreshToken(user.id, refreshToken)

        return newAccessToken
    }

    @Transactional // Do it all or nothing. If some request to mongodb fails, then nothing will be applied.
    fun refresh(refreshToken: String): TokenPair {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token.")
        }

        // Get current token getting user both from database
        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(ObjectId(userId)).orElseThrow {
            ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token.")
        }

        // Delete current token from database
        val hashedToken = hashToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(user.id, hashedToken)
            ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(401),
                "Refresh token not recognized (maybe used or expired?)"
            )

        refreshTokenRepository.deleteByUserIdAndHashedToken(user.id, hashedToken)

        // Post new token in database
        val newAccessToken = jwtService.generateAccessToken(userId)
        val newRefreshToken = jwtService.generateRefreshToken(userId)

        storeRefreshToken(user.id, newRefreshToken)

        // return refresh token
        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    private fun storeRefreshToken(userId: ObjectId, rawRefreshToken: String) {
        val hashedToken = hashToken(rawRefreshToken)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashedToken
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())

        return Base64.getEncoder().encodeToString(hashBytes)
    }

    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )
}