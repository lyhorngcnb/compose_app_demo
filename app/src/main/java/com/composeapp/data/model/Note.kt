// data/model/Note.kt
package com.composeapp.data.model

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val timestamp: Long
)