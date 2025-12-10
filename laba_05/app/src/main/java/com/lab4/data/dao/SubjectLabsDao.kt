package com.lab4.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lab4.data.entity.SubjectLabEntity

@Dao
interface SubjectLabsDao { // 👈 Інтерфейс DAO для таблиці SubjectLabEntity

    @Query("SELECT * FROM subjectsLabs WHERE subject_id = :subjectId")
    // 👈 SQL-запит для фільтрації: вибрати всі лаби, де subject_id (ключ зв'язку) дорівнює переданому параметру
    suspend fun getSubjectLabsBySubjectId(subjectId: Int): List<SubjectLabEntity>
    // ☝️ Функція повертає список лабораторних робіт, що належать одному конкретному предмету.

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // 👈 Анотація для додавання нових даних.
    // OnConflictStrategy.REPLACE: якщо лаба з таким ID вже є, вона буде замінена.
    suspend fun addSubjectLab(subjectLabEntity: SubjectLabEntity)
    // ☝️ Додає нову лабораторну роботу в базу. Використовується для початкового заповнення бази.

    @Update
    // 👈 Анотація для оновлення ІСНУЮЧОГО запису.
    suspend fun updateLab(lab: SubjectLabEntity)
    // ☝️ Оновлює всі поля лабораторної роботи (статус, коментар, назву), якщо передати весь об'єкт.

    @Query("UPDATE subjectsLabs SET status = :newStatus WHERE id = :labId")
    // 👈 SQL-запит для оновлення одного поля.
    // UPDATE: змінити значення 'status' на 'newStatus' тільки для лаби з певним 'labId'.
    suspend fun updateStatus(labId: Int, newStatus: String)
    // ☝️ Функція оновлює лише поле статусу для вибраної лабораторної роботи.

    @Query("UPDATE subjectsLabs SET comment = :newComment WHERE id = :labId")
    // 👈 SQL-запит для оновлення іншого поля.
    // UPDATE: змінити значення 'comment' на 'newComment' тільки для лаби з певним 'labId'.
    suspend fun updateComment(labId: Int, newComment: String)
    // ☝️ Функція оновлює лише поле коментаря для вибраної лабораторної роботи.
}