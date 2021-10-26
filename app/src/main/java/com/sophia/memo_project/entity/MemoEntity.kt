package com.sophia.memo_project.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class MemoEntity(
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "subtitle")
    var subtitle: String? = null,
    @ColumnInfo(name = "content")
    var content: String? = null,
    @ColumnInfo(name = "date")
    var date: String? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)
