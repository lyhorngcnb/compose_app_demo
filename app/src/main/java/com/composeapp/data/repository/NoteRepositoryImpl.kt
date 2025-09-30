// data/repository/NoteRepositoryImpl.kt
package com.composeapp.data.repository

import com.composeapp.domain.repository.NoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.composeapp.domain.model.Note

class NoteRepositoryImpl @Inject constructor() : NoteRepository {
    private val notes = mutableListOf<Note>()

    override fun getNotes(): Flow<List<Note>> = flow {
        delay(500) // Simulate network delay
        emit(notes.toList())
    }

    override suspend fun addNote(note: Note) {
        delay(300)
        notes.add(note)
    }

    override suspend fun deleteNote(id: String) {
        delay(300)
        notes.removeAll { it.id == id }
    }
}
