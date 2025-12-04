package com.lab4.ui.screens.subjectsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab4.data.db.Lab4Database
import com.lab4.data.entity.SubjectEntity
import com.lab4.data.entity.SubjectLabEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubjectsListViewModel(
    private val database: Lab4Database
) : ViewModel() {

    /**
    Flow - the (container, channel, observer), can accept and move data from producer to consumers
    StateFlow - the flow which also store data.
    MutableStateFlow - the stateFlow which can accept data (which you can fill)

    _subjectListStateFlow - private MutableStateFlow - ViewModel (add new data here)
    subjectListStateFlow - public StateFlow - ComposeScreen (read only data on screen)
     */
    private val _subjectListStateFlow = MutableStateFlow<List<SubjectEntity>>(emptyList())
    val subjectListStateFlow: StateFlow<List<SubjectEntity>>
        get() = _subjectListStateFlow


    /**
    Init block of ViewModel - invokes once the ViewModel is created
     */
    init {
        viewModelScope.launch {
            // for now, we can preload some data to DB here, on the first screen
            val subjects = database.subjectsDao.getAllSubjects()
            if (subjects.isEmpty()) preloadData()
            _subjectListStateFlow.value = database.subjectsDao.getAllSubjects()
        }
    }

    private suspend fun preloadData() {
        val subjects = listOf(
            SubjectEntity(title = "Математичний аналіз"),
            SubjectEntity(title = "Програмування Android"),
            SubjectEntity(title = "Комп’ютерні мережі")
        )

        val labs = listOf(
            SubjectLabEntity(
                subjectId = 1,
                title = "Лабораторна 1",
                description = "Інтеграли"
            ),
            SubjectLabEntity(
                subjectId = 2,
                title = "Лабораторна 2",
                description = "Room Database"
            ),
            SubjectLabEntity(
                subjectId = 3,
                title = "Лабораторна 1",
                description = "TCP-з’єднання"
            )
        )


        subjects.forEach { database.subjectsDao.addSubject(it) }
        labs.forEach { database.subjectLabsDao.addSubjectLab(it) }
    }
}