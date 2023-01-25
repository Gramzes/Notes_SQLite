package data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import model.Note

class DatabaseHandler(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "notesDB"
        const val TABLE_NAME = "notes"
        const val COLUMN_ID = "id"
        const val COLUMN_TOPIC = "topic"
        const val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_notes_table = "CREATE TABLE $TABLE_NAME " +
                "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TOPIC TEXT," +
                "$COLUMN_CONTENT TEXT);"
        db?.execSQL(create_notes_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addNote(note: Note) : Long{
        val db = writableDatabase

        val contentValues = ContentValues().apply {
            put(COLUMN_TOPIC, note.topic)
            put(COLUMN_CONTENT, note.content)
        }

        val id = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return id
    }

    fun getNote(id: Long) : Note?{
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME,
            arrayOf(COLUMN_ID, COLUMN_TOPIC, COLUMN_CONTENT),
            "$COLUMN_ID =?",
            arrayOf(id.toString()),
            null, null, null)
        if (cursor.moveToFirst()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val topic = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            cursor.close()
            return Note(id, topic, content)
        }
        cursor.close()
        db.close()
        return null
    }

    fun getAllNotes(): List<Note>{
        val db = readableDatabase
        val list = ArrayList<Note>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if(cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val topic = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
                list.add(Note(id, topic, content))
            } while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun updateNote(note: Note) : Int{
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TOPIC, note.topic)
            put(COLUMN_CONTENT, note.content)
        }
        val count = db.update(TABLE_NAME, contentValues,
            "$COLUMN_ID =?",
            arrayOf(note.id.toString()))
        db.close()
        return count
    }

    fun deleteNote(id: Int): Int{
        val db = writableDatabase
        val count = db.delete(TABLE_NAME, "$COLUMN_ID =?", arrayOf(id.toString()))
        db.close()
        return count
    }

    fun getNotesCount(): Int{
        val db = readableDatabase
        val countQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(countQuery, null)
        val count = cursor.count
        cursor.close()
        db.close()
        return count
    }
}