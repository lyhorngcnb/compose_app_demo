package com.composeapp.presentation

import androidx.lifecycle.ViewModel
import com.composeapp.domain.model.Note
import com.composeapp.domain.usecase.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    fun getNotes(): List<Note> = getNotesUseCase()
}
