package com.vovan.lab7

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
// 👈 Анотація: Вказує Android, що цей тестовий клас має бути запущений за допомогою
// стандартного Android JUnit 4 Runner. Це необхідно для тестування Android-компонентів.
class ExampleInstrumentedTest {

    @Test
    // 👈 Анотація: Позначає функцію як окремий тест, який може бути виконаний автоматично.
    fun useAppContext() {

        // 1. Отримання Контексту
        // Отримуємо контекст (системну інформацію) того додатку, який зараз тестується.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // 2. Асерт (Перевірка)
        // assertEquals: Головний метод тестування. Він порівнює очікуване значення з фактичним.
        assertEquals("com.vovan.lab7", appContext.packageName)
        // ☝️ Ми перевіряємо, чи збігається фактичне ім'я пакета додатку (appContext.packageName)
        // з очікуваним ім'ям пакета "com.vovan.lab7" (згідно з вашою структурою Лаби 7).
        // Якщо вони збігаються, тест вважається успішним.
    }
}