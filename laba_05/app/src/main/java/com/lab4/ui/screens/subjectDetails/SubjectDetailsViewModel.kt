package com.lab4.ui.screens.subjectDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab4.data.db.Lab4Database
import com.lab4.data.entity.SubjectEntity
import com.lab4.data.entity.SubjectLabEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubjectDetailsViewModel(private val database: Lab4Database) : ViewModel() {

    private val _subjectStateFlow = MutableStateFlow<SubjectEntity?>(null)
    val subjectStateFlow: StateFlow<SubjectEntity?> get() = _subjectStateFlow

    private val _subjectLabsListStateFlow = MutableStateFlow<List<SubjectLabEntity>>(emptyList())
    val subjectLabsListStateFlow: StateFlow<List<SubjectLabEntity>> get() = _subjectLabsListStateFlow

    fun initData(id: Int) {
        viewModelScope.launch {
            _subjectStateFlow.value = database.subjectsDao.getSubjectById(id)
            _subjectLabsListStateFlow.value = database.subjectLabsDao.getSubjectLabsBySubjectId(id)
        }
    }

    fun updateStatus(labId: Int, status: String) {
        viewModelScope.launch {
            database.subjectLabsDao.updateStatus(labId, status)
            _subjectLabsListStateFlow.value = database.subjectLabsDao.getSubjectLabsBySubjectId(
                _subjectStateFlow.value?.id ?: return@launch
            )
        }
    }

    fun updateComment(labId: Int, comment: String) {
        viewModelScope.launch {
            database.subjectLabsDao.updateComment(labId, comment)
            _subjectLabsListStateFlow.value = database.subjectLabsDao.getSubjectLabsBySubjectId(
                _subjectStateFlow.value?.id ?: return@launch
            )
        }
    }
}
