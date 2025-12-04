package com.vovan.lab7.di

import com.vovan.lab7.data.GeminiAIRepository
import com.vovan.lab7.ui.screens.subjectDetails.GameScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<GeminiAIRepository> { GeminiAIRepository() }
    viewModel { GameScreenViewModel(get()) }
}