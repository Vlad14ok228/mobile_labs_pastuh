// 1. ПАКЕТ: Вказує на розташування файлу в структурі проекту
package com.example.smartrecipeapp.data.database

// 2. ІМПОРТИ: Підключаємо інструменти Room та Flow
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartrecipeapp.data.model.Meal
import kotlinx.coroutines.flow.Flow

// 3. АНОТАЦІЯ @Dao: Позначає цей інтерфейс як "Об'єкт Доступу до Даних".
// Room згенерує реальний Java/Kotlin код для цього інтерфейсу під час компіляції.
@Dao
interface MealDao {

    // --- ОТРИМАННЯ ДАНИХ (READ) ---
    // @Query дозволяє писати звичайні SQL-запити.
    // "SELECT * FROM meals" означає: взяти всі стовпці з таблиці "meals".
    @Query("SELECT * FROM meals")
    // Повертає Flow. Це "живий потік" даних.
    // Головна фішка: якщо ти додаси новий рецепт, Flow сам повідомить UI,
    // і список на екрані оновиться миттєво без перезавантаження.
    fun getAllFavorites(): Flow<List<Meal>>

    // --- ПЕРЕВІРКА НАЯВНОСТІ ---
    // Перевіряємо, чи існує запис з конкретним ID.
    // :id (з двокрапкою) означає, що сюди підставиться значення зі змінної id, яку ми передали у функцію.
    @Query("SELECT EXISTS(SELECT 1 FROM meals WHERE id = :id)")
    // Повертає Flow<Boolean> (true - є, false - немає).
    // Це потрібно, щоб зафарбовувати "сердечко" на екрані деталей.
    fun isFavorite(id: String): Flow<Boolean>

    // --- ВСТАВКА (CREATE / UPDATE) ---
    // onConflict = REPLACE означає: "Якщо спробуєш зберегти рецепт, який вже є в базі (співпадає ID),
    // то не видавай помилку, а просто заміни старий запис на новий".
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // suspend fun: Операція запису може тривати довго (мілісекунди),
    // тому вона має бути асинхронною (suspend), щоб не "заморозити" інтерфейс телефону.
    suspend fun insertMeal(meal: Meal)

    // --- ВИДАЛЕННЯ (DELETE) ---
    // @Delete: Room сам подивиться на ID об'єкта meal і видалить відповідний рядок з таблиці.
    @Delete
    suspend fun deleteMeal(meal: Meal)
}