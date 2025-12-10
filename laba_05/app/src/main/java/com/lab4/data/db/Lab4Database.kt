package com.lab4.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lab4.data.dao.SubjectDao
import com.lab4.data.dao.SubjectLabsDao
import com.lab4.data.entity.SubjectEntity
import com.lab4.data.entity.SubjectLabEntity

@Database(entities = [SubjectEntity::class, SubjectLabEntity::class], version = 1)
// 👈 Анотація @Database позначає цей клас як головний клас бази даних Room.
// entities: Тут ми перелічуємо ВСІ класи-сутності (таблиці), які будуть у базі.
// version = 1: Це номер поточної версії бази даних. Його треба збільшувати, якщо змінюється структура таблиць.
abstract class Lab4Database : RoomDatabase() { // 👈 Клас бази даних Room завжди має бути абстрактним

    abstract val subjectsDao: SubjectDao
    // ☝️ Абстрактна властивість для доступу до DAO предметів.
    // Room автоматично створює реалізацію цього DAO-об'єкта.

    abstract val subjectLabsDao: SubjectLabsDao
    // ☝️ Абстрактна властивість для доступу до DAO лабораторних робіт.
}
