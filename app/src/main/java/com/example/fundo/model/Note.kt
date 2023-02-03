package com.example.fundo.model

data class Note(var noteId: String?, val title: String, val content: String, val isArchive: Boolean = false)