package com.lab4.ui.screens.subjectsList

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lab4.data.db.DatabaseStorage
import com.lab4.data.entity.SubjectEntity

@Composable
fun SubjectsListScreen(onDetailsScreen: (Int) -> Unit) {
    val context = LocalContext.current
    val db = DatabaseStorage.getDatabase(context)
    var subjects by remember { mutableStateOf<List<SubjectEntity>>(emptyList()) }

    LaunchedEffect(Unit) {
        subjects = db.subjectsDao.getAllSubjects()
    }

    LazyColumn(Modifier.fillMaxSize()) {
        items(subjects) { subject ->
            Text(
                text = subject.title,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable(
                        interactionSource = null,
                        indication = LocalIndication.current,
                    ) { subject.id?.let { id -> onDetailsScreen(id) } }
            )
        }
    }
}
