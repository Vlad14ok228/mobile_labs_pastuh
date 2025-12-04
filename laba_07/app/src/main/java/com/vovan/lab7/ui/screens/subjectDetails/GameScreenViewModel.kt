package com.vovan.lab7.ui.screens.subjectDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vovan.lab7.data.GeminiAIRepository
import com.vovan.lab7.data.entity.TextPair
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class GameScreenViewModel(
    private val repository: GeminiAIRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _quizList = MutableStateFlow<List<TextPair>?>(null)
    val quizList: StateFlow<List<TextPair>?> get() = _quizList

    fun loadQuiz(topic: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _quizList.value = repository.generateQuizByTopic(topic)
            _isLoading.value = false
        }
    }
}
