package com.composeapp.domain.usecase

import com.composeapp.domain.model.Note
import com.composeapp.domain.repository.NoteRepository
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): List<Note> {
        return repository.getNotes()
    }
}
