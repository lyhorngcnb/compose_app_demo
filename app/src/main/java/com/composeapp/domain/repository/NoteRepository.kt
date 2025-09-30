// domain/repository/NoteRepository.kt
package com.composeapp.domain.repository

import com.composeapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    suspend fun addNote(note: Note)
    suspend fun deleteNote(id: String)
}