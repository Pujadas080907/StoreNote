package com.example.storenotes.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storenotes.Models.Note

@Database(entities = arrayOf( Note::class), version = 1, exportSchema = false)
 abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao() : NoteDao
    abstract fun getTaskDao() : TaskDao

    companion object{

        @Volatile
        private var INSTANCE : NoteDatabase? = null

        fun getDatabase(context: Context) : NoteDatabase{

            return INSTANCE ?: synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}