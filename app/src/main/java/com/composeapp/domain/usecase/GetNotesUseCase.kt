// domain/usecase/GetNotesUseCase.kt
package com.composeapp.domain.usecase

import com.composeapp.domain.model.Note
import com.composeapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> = repository.getNotes()
}