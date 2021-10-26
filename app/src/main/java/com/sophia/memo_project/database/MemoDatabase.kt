package com.sophia.memo_project.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sophia.memo_project.dao.MemoDao
import com.sophia.memo_project.entity.MemoEntity

@Database(entities = [MemoEntity::class], version = 1)
abstract class MemoDatabase: RoomDatabase() {

//    companion object {
//        var notesDatabase: MemoDatabase? = null
//
//        @Synchronized
//        fun getDatabase(context: Context): MemoDatabase {
//            if (notesDatabase == null) {
//                notesDatabase = Room.databaseBuilder(
//                    context
//                    , MemoDatabase::class.java
//                    , "notes.db"
//                ).build()
//            }
//            return notesDatabase!!
//        }
//    }

    abstract fun memoDao(): MemoDao
}