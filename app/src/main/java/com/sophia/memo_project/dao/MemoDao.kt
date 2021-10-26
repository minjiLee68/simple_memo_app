package com.sophia.memo_project.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.sophia.memo_project.entity.MemoEntity

@Dao
interface MemoDao: BaseDao<MemoEntity> {
    @Query("SELECT * FROM memo ORDER BY id DESC")
    fun getAll(): LiveData<List<MemoEntity>>

    @Query("SELECT * FROM memo WHERE title LIKE :title or LOWER(title) like LOWER(:title)")
    fun getSearchTitle(title: String): LiveData<List<MemoEntity>>
}