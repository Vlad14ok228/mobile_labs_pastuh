package com.lab4.di

import android.content.Context
import androidx.room.Room
import com.lab4.data.db.Lab4Database
import com.lab4.ui.screens.subjectDetails.SubjectDetailsViewModel
import com.lab4.ui.screens.subjectsList.SubjectsListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * appModule = module{...} - is Koin module for creating instances of all components in App
 * - invokes in App class
 * - in the module{...} scope you can create different instances by functions single{}, factory{}, viewModel{}
 * - in the module{...} scope to get some other instance which was created in scope you can call get()
 */
val appModule = module {
    /**
    single{ ...class() } - create single (singleton) instance of class
    In this example is created single instance of Lab5Database database
    - get<Context>() - function gets the context which is in Koin module by default
     */
    single<Lab4Database> {
        Room.databaseBuilder(
            get<Context>(),
            Lab4Database::class.java, "lab4Database"
        ).build()
    }

    /**
    viewModel{ ...viewModelClass() } - create ViewModel instance
    - get<Lab5Database>() - function gets the instance of database which is created above

     */

    viewModel { SubjectsListViewModel(get()) }
    viewModel { SubjectDetailsViewModel(get()) }
}