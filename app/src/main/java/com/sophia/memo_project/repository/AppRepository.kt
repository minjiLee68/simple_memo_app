package com.sophia.memo_project.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.sophia.memo_project.database.MemoDatabase
import com.sophia.memo_project.entity.MemoEntity

class AppRepository(application: Application) {

    companion object {
        private const val MEMO_DATABASE_NAME = "MemoDataBase"
    }

    private val db = Room.databaseBuilder(
        application, MemoDatabase::class.java, MEMO_DATABASE_NAME
    ).build()

    private val memoDao = db.memoDao()

    fun getAllMemo(): LiveData<List<MemoEntity>> =
        memoDao.getAll()

    fun getSearchTitle(title: String): LiveData<List<MemoEntity>> =
        memoDao.getSearchTitle(title)

    fun inserMemo(memo: MemoEntity) {
        memoDao.insert(memo)
    }

    fun updateMemo(memo: MemoEntity) {
        memoDao.update(memo)
    }

    fun deleteMemo(memo: MemoEntity) {
        memoDao.delete(memo)
    }
}