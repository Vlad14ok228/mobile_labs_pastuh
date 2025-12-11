package com.lab6.data.entity.response

import com.lab6.data.entity.Coordinates
import com.lab6.data.entity.WeatherMain

/**
 * WeatherResponse - data class of root response of "/data/2.5/weather" request
 */
// Це data class, який слугує моделлю для парсингу (читання) відповіді від сервера.
// Він представляє КОРЕНЕВИЙ об'єкт JSON-відповіді для поточної погоди.
data class WeatherResponse(

    // coord: Це поле відповідає за географічні координати (широту і довготу) міста.
    // Тут ми використовуємо інший, вкладений data class 'Coordinates', щоб організовано зберігати lat та lon.
    val coord: Coordinates,

    // main: Це поле відповідає за основні показники погоди.
    // Тут зберігаються температура, вологість, атмосферний тиск.
    val main: WeatherMain,

    // В API є багато інших полів (weather, wind, sys...), але ми взяли тільки ті,
    // які потрібні для виконання завдання, ігноруючи решту.
)