// 1. ПАКЕТ: Папка, де лежить файл
package com.example.smartrecipeapp.data.model

// 2. ІМПОРТИ: Нам потрібні анотації для бази (Room) і для JSON (Gson)
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// 3. @Entity: Ця анотація каже Room: "Зроби з цього класу таблицю в базі даних".
// tableName = "meals": Таблиця буде називатися "meals".
@Entity(tableName = "meals")
data class Meal(

    // 4. @PrimaryKey: Це унікальний ідентифікатор запису (як паспортний номер).
    // Room використовує його, щоб відрізняти одну страву від іншої.
    // Ми НЕ генеруємо його самі (autoGenerate = false), бо беремо готовий ID із сервера.
    @PrimaryKey
    // 5. @SerializedName("idMeal"): Це інструкція для бібліотеки Gson.
    // API надсилає нам поле з назвою "idMeal", але в коді ми хочемо називати його просто "id".
    // Ця анотація зв'язує чуже ім'я "idMeal" з нашим "id".
    @SerializedName("idMeal")
    val id: String,

    // Сервер надсилає "strMeal" -> ми зберігаємо як "name" (Назва страви)
    @SerializedName("strMeal")
    val name: String,

    // Сервер надсилає "strMealThumb" -> ми зберігаємо як "imageUrl" (Посилання на картинку)
    @SerializedName("strMealThumb")
    val imageUrl: String,

    // Сервер надсилає "strCategory" -> ми зберігаємо як "category" (Категорія: Десерт, М'ясо...)
    // String? (зі знаком питання) означає Nullable.
    // Якщо сервер раптом не надішле категорію, програма не впаде, а запише сюди null (пустоту).
    @SerializedName("strCategory")
    val category: String?,

    // "strInstructions" -> "instructions" (Інструкція приготування)
    @SerializedName("strInstructions")
    val instructions: String?,

    // 👇 Твій новий рядок
    // "strArea" -> "area" (Регіон/Кухня: Italian, Ukrainian, British)
    @SerializedName("strArea")
    val area: String?
)