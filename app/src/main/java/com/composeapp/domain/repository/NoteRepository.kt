package com.composeapp.domain.repository

import com.composeapp.domain.model.Note

interface NoteRepository {
    fun getNotes(): List<Note>
}
