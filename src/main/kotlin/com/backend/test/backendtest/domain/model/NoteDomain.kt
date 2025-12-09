package com.backend.test.backendtest.domain.model

import java.time.Instant

data class NoteDomain(
    val id: String?,
    val ownerId: String,
    val title: String,
    val content: String,
    val color: Long,
    val createdAt: Instant?,
    val updatedAt: Instant
)