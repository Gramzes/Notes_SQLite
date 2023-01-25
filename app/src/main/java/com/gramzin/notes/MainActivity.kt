package com.gramzin.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import data.DatabaseHandler
import model.Note

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = DatabaseHandler(this)
        val id = db.addNote(Note("Test", "Test content"))
        val note = db.getNote(id)

        Log.d("alex", "count: ${db.getNotesCount()}")
        note?.let {
            Log.d("alex", "${note.id} ${it.topic} ${it.content}")
            it.topic = "Update test"
            it.content = "Content"
            Log.d("alex", db.updateNote(it).toString())
        }

        val noteNew = db.getNote(id)
        noteNew?.let {
            Log.d("alex", "${noteNew.id} ${it.topic} ${it.content}")
        }

        for(i in 1..20){
            db.deleteNote(i)
        }
        Log.d("alex", "count: ${db.getNotesCount()}")
        val list = db.getAllNotes()
        for(i in list){
            Log.d("alex", "${i.id} ${i.topic} ${i.content}")
        }
    }
}