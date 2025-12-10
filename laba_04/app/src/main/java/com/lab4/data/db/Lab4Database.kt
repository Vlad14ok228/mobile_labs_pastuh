package com.lab4.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lab4.data.dao.SubjectDao
import com.lab4.data.dao.SubjectLabsDao
import com.lab4.data.entity.SubjectEntity
import com.lab4.data.entity.SubjectLabEntity
import kotlinx.coroutines.*

@Database(entities = [SubjectEntity::class, SubjectLabEntity::class], version = 1)
abstract class Lab4Database : RoomDatabase() {
    abstract val subjectsDao: SubjectDao
    abstract val subjectLabsDao: SubjectLabsDao
}

object DatabaseStorage {

    private val coroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        },
    )

    private var _database: Lab4Database? = null

    fun getDatabase(context: Context): Lab4Database {
        if (_database != null) return _database as Lab4Database
        else {
            _database = Room.databaseBuilder(
                context,
                Lab4Database::class.java,
                "lab4Database"
            ).build()

            // ⚠️ preloadData запускаємо блокуюче, щоб гарантовано все підгрузилось
            runBlocking {
                preloadData()
            }

            return _database as Lab4Database
        }
    }


    private suspend fun preloadData() {
        val db = _database ?: return

        // щоб уникнути дублювання
        if (db.subjectsDao.getAllSubjects().isNotEmpty()) return

        val subjects = listOf(
            SubjectEntity(title = "Проектування ІС"),
            SubjectEntity(title = "Embedded Systems"),
            SubjectEntity(title = "DevOPS")
        )

        val labs = listOf(
            SubjectLabEntity(subjectId = 1, title = "Лабораторна 1", description = "Налаштування протоколів"),
            SubjectLabEntity(subjectId = 2, title = "Лабораторна 2", description = "Вивчення переривань ESP32"),
            SubjectLabEntity(subjectId = 3, title = "Лабораторна 1", description = "Docker Compose Lab")
        )

        subjects.forEach { s ->
            db.subjectsDao.addSubject(s)
        }

        labs.forEach { l ->
            db.subjectLabsDao.addSubjectLab(l)
        }
    }
}
