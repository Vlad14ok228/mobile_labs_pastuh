package com.lab4.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * SubjectEntity - the data class which represents the `subjects` table
 * - marked with annotation @Entity - for SQL tables
 * - contains @PrimaryKey field id - all objects in tables has unique primary keys
 */
@Entity(tableName = "subjects")
// 👈 Анотація @Entity: Позначає цей клас як ТАБЛИЦЮ (Сутність) у базі даних Room.
// tableName = "subjects": Вказує, як саме називатиметься ця таблиця в SQL-базі.
data class SubjectEntity(
    // Це основний ключ, який унікально ідентифікує кожен предмет.
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    // 👈 @PrimaryKey: Обов'язкова анотація, яка робить 'id' унікальним ключем.
    // autoGenerate = true: Room автоматично присвоюватиме ID (1, 2, 3...) при додаванні нового предмета.
    // Int? = null: Робить ID необов'язковим при створенні об'єкта в коді (Room сам його додасть).

    val title: String
    // 👈 Це звичайний стовпчик у таблиці. Він зберігатиме назву предмета (наприклад, "Математичний аналіз").
)