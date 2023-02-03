package com.example.fundo.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fundo.model.Note

class DBHelper(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "FUNDO_NOTES"
        private const val DB_VERSION = 1
        const val TABLE_NAME = "notes_data"
        const val USER_ID_COL = "user_id"
        const val NOTE_ID_COL = "note_id"
        const val TITLE_COL = "note_title"
        const val CONTENT_COL = "note_content"
        const val IS_ARCHIVE_COL = "note_is_archive"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE $TABLE_NAME (" +
                "$USER_ID_COL TEXT," +
                "$NOTE_ID_COL TEXT," +
                "$TITLE_COL TEXT," +
                "$CONTENT_COL TEXT," +
                "$IS_ARCHIVE_COL SMALLINT," +
                "PRIMARY KEY ($USER_ID_COL, $NOTE_ID_COL))")

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun saveNote(userId: String, note: Note, noteAlreadyExists: Boolean) {
        val values = ContentValues()
        values.put(USER_ID_COL, userId)
        values.put(NOTE_ID_COL, note.noteId)
        values.put(TITLE_COL, note.title)
        values.put(CONTENT_COL, note.content)
        values.put(IS_ARCHIVE_COL, note.isArchive)

        val db = writableDatabase
        if (noteAlreadyExists)
            db.update(
                TABLE_NAME,
                values,
                "$USER_ID_COL = ? AND $NOTE_ID_COL = ?",
                arrayOf(userId, note.noteId)
            )
        else
            db.insert(TABLE_NAME, null, values)
//        db.close()
    }

    fun readNote(): Cursor? {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun deleteNote(userId: String, noteId: String): Boolean {
        val db = writableDatabase
        return db.delete(
            TABLE_NAME,
            "$USER_ID_COL = ? AND $NOTE_ID_COL = ?",
            arrayOf(userId, noteId)
        ) > 0
    }

    fun archiveNote(userId: String, noteId: String): Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(IS_ARCHIVE_COL, true)
        return db.update(
            TABLE_NAME,
            values,
            "$USER_ID_COL = ? AND $NOTE_ID_COL = ?",
            arrayOf(userId, noteId)
        ) > 0
    }
}