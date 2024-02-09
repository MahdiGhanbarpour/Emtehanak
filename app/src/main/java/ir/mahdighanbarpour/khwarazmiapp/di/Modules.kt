package ir.mahdighanbarpour.khwarazmiapp.di

import android.content.Context
import ir.mahdighanbarpour.khwarazmiapp.features.examDetailScreen.ExamViewModel
import ir.mahdighanbarpour.khwarazmiapp.features.frequentlyQuestionsScreen.FrequentlyQuestionsViewModel
import ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen.StudentHomeViewModel
import ir.mahdighanbarpour.khwarazmiapp.features.mainRegScreen.RegisterViewModel
import ir.mahdighanbarpour.khwarazmiapp.features.otpLoginScreen.LoginOtpViewModel
import ir.mahdighanbarpour.khwarazmiapp.model.dataBase.MainDataBase
import ir.mahdighanbarpour.khwarazmiapp.model.net.apiServiceBuilder
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.ExamRepository
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.FrequentlyQuestionsRepository
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.StudentRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// Adding repetitive classes in the app to dependency injection
val myModules = module {
    single { androidContext().getSharedPreferences("Emtehanak", Context.MODE_PRIVATE) }

    single { MainDataBase.getDataBase(get()) }
    single { get<MainDataBase>().mainDao }

    single { FrequentlyQuestionsRepository(get(), get()) }
    single { StudentRepository(get()) }
    single { ExamRepository(get()) }

    single { apiServiceBuilder() }

    viewModel { FrequentlyQuestionsViewModel(get()) }
    viewModel { LoginOtpViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { StudentHomeViewModel(get()) }
    viewModel { ExamViewModel(get()) }
}