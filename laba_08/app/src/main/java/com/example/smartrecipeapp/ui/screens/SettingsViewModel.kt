// 1. ПАКЕТ: Папка ui/screens (бо це логіка для екранів)
package com.example.smartrecipeapp.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 2. КЛАС ViewModel:
// Цей клас зберігає стан налаштувань.
// Він потрібен, щоб дані не зникали при повороті екрану, і щоб розділити логіку від малювання (Compose).
class SettingsViewModel : ViewModel() {

    // --- НАЛАШТУВАННЯ ТЕМИ ---

    // 3. _isDarkTheme (PRIVATE MUTABLE):
    // Це "внутрішня кухня". MutableStateFlow означає, що значення МОЖНА змінювати.
    // Ми робимо її приватною (private), щоб ніхто ззовні (наприклад, з іншого екрану)
    // не міг випадково зламати налаштування.
    // Початкове значення = false (Світла тема).
    private val _isDarkTheme = MutableStateFlow(false)

    // 4. isDarkTheme (PUBLIC IMMUTABLE):
    // Це "вітрина". StateFlow означає, що значення можна ТІЛЬКИ ЧИТАТИ (read-only).
    // Екран (SettingsScreen) підписується на цю змінну. Як тільки вона зміниться, екран перемалюється.
    // .asStateFlow() перетворює змінну Mutable на звичайну StateFlow.
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    // --- НАЛАШТУВАННЯ СПОВІЩЕНЬ ---

    // 5. Те саме для сповіщень.
    // Спочатку вони увімкнені (true).
    private val _areNotificationsEnabled = MutableStateFlow(true)
    // Публічна версія для читання UI.
    val areNotificationsEnabled: StateFlow<Boolean> = _areNotificationsEnabled.asStateFlow()

    // --- МЕТОДИ (ACTIONS) ---

    // 6. Функція перемикання теми:
    // Коли користувач натискає Switch на екрані, викликається ця функція.
    // Вона приймає нове значення (true/false) і записує його в нашу приватну змінну.
    fun toggleTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }

    // 7. Функція перемикання сповіщень:
    fun toggleNotifications(isEnabled: Boolean) {
        _areNotificationsEnabled.value = isEnabled
    }
}