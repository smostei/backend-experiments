package com.backend.test.backendtest.domain.repository

import com.backend.test.backendtest.domain.model.NoteDomain

interface NoteRepository {
    fun save(note: NoteDomain): NoteDomain
    fun findByOwner(ownerId: String): List<NoteDomain>
    fun findById(id: String): NoteDomain?
    fun deleteById(id: String)
}