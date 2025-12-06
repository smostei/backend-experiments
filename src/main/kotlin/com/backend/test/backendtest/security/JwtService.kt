package com.backend.test.backendtest.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Base64
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.secret}") private val jwtSecret: String
) {

    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))
    private val accessTokenValidityMs = 15L * 60L * 1000L // (15 minutes)
    val refreshTokenValidityMs = 30L * 24 * 60 * 60 * 1000L // (30 days)

    private fun generateToken(
        userId: String,
        type: String,
        expiry: Long
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + expiry)
        return Jwts.builder()
            .subject(userId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(userId: String): String {
        return generateToken(
            userId = userId,
            type = ACCESS_TOKEN_TYPE,
            expiry = accessTokenValidityMs
        )
    }

    fun generateRefreshToken(userId: String): String {
        return generateToken(
            userId = userId,
            type = REFRESH_TOKEN_TYPE,
            expiry = refreshTokenValidityMs
        )
    }

    fun validateAccessToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false

        return tokenType == ACCESS_TOKEN_TYPE
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false

        return tokenType == REFRESH_TOKEN_TYPE
    }


    fun getUserIdFromToken(token: String): String {
        val claims = parseAllClaims(token) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(401),
            "Invalid token."
        )

        return claims.subject
    }

    private fun parseAllClaims(token: String): Claims? {
        val rawToken = if (token.startsWith("Bearer ")) {
            token.removePrefix("Bearer ")
        } else {
            token
        }

        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (e: Exception) {
            println("Error parsing claims: ${e.message}")
            null
        }
    }

    companion object {
        private const val ACCESS_TOKEN_TYPE = "access"
        private const val REFRESH_TOKEN_TYPE = "refresh"

    }
}