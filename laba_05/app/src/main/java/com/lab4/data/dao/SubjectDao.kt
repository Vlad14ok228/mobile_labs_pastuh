package com.lab4.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lab4.data.entity.SubjectEntity

/**
 * SubjectDao - interface of communication with `subjects` table
 * - marked with @Dao annotation (Data Access Object)
 * - contains custom functions-mappers for management data in table
 */
@Dao
interface SubjectDao { // 👈 Інтерфейс DAO (Data Access Object) для таблиці SubjectEntity

    @Query("SELECT * FROM subjects") // 👈 SQL-запит: вибрати всі записи з таблиці 'subjects'
    suspend fun getAllSubjects(): List<SubjectEntity>
    // ☝️ Функція асинхронно повертає повний список усіх предметів.

    @Query("SELECT * FROM subjects WHERE id = :id")
    // 👈 SQL-запит: знайти один предмет, ID якого збігається з параметром :id
    suspend fun getSubjectById(id: Int): SubjectEntity?
    // ☝️ Повертає один об'єкт SubjectEntity або null, якщо не знайдено.

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // 👈 Анотація для додавання нових даних у таблицю.
    // OnConflictStrategy.REPLACE: якщо предмет з таким ID вже існує, він буде замінений.
    suspend fun addSubject(subjectEntity: SubjectEntity)
    // ☝️ Функція асинхронно додає новий предмет у базу даних.
}