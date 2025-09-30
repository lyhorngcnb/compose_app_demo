package com.composeapp.data.repository

import com.composeapp.domain.model.Note
import com.composeapp.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor() : NoteRepository {
    override fun getNotes(): List<Note> {
        return listOf(
            Note(1, "Learn DI in Compose"),
            Note(2, "Practice Clean Architecture"),
            Note(3, "Build real projects 🚀")
        )
    }
}
