package com.lab4.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lab4.data.dao.SubjectDao
import com.lab4.data.dao.SubjectLabsDao
import com.lab4.data.entity.SubjectEntity
import com.lab4.data.entity.SubjectLabEntity

@Database(entities = [SubjectEntity::class, SubjectLabEntity::class], version = 1)
abstract class Lab4Database : RoomDatabase() {
    abstract val subjectsDao: SubjectDao
    abstract val subjectLabsDao: SubjectLabsDao
}
