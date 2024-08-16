package ir.mahdighanbarpour.khwarazmiapp.di

import android.content.Context
import ir.mahdighanbarpour.khwarazmiapp.features.addExamQuestionScreen.AddExamQuestionViewModel
import ir.mahdighanbarpour.khwarazmiapp.features.examDetailScreen.ExamViewModel
import ir.mahdighanbarpour.khwarazmiapp.features.examsListScreen.ExamsListViewModel
import ir.mahdighanbarpour.khwarazmiapp.features.frequentlyQuestionsScreen.FrequentlyQuestionsViewModel
import ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen.StudentHomeViewModel
import ir.mahdighanbarpour.khwarazmiapp.features.homeTeacherScreen.TeacherHomeViewModel
import ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen.ExamMainViewModel
import ir.mahdighanbarpour.khwarazmiapp.features.mainRegScreen.RegisterViewModel
import ir.mahdighanbarpour.khwarazmiapp.features.otpLoginScreen.LoginOtpViewModel
import ir.mahdighanbarpour.khwarazmiapp.features.splashScreen.SplashScreenViewModel
import ir.mahdighanbarpour.khwarazmiapp.model.dataBase.MainDataBase
import ir.mahdighanbarpour.khwarazmiapp.model.net.apiServiceBuilder
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.ExamRepository
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.FrequentlyQuestionsRepository
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.LessonRepository
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.StudentRepository
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.TeacherRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// Adding repetitive classes in the app to dependency injection
val myModules = module {
    // Adding shared preferences to dependency injection
    single { androidContext().getSharedPreferences("Emtehanak", Context.MODE_PRIVATE) }

    // Adding the database to dependency injection
    single { MainDataBase.getDataBase(get()) }
    single { get<MainDataBase>().mainDao }

    // Adding the repositories to dependency injection
    single { FrequentlyQuestionsRepository(get(), get()) }
    single { StudentRepository(get()) }
    single { TeacherRepository(get()) }
    single { ExamRepository(get()) }
    single { LessonRepository(get()) }

    // Adding the api service to dependency injection
    single { apiServiceBuilder() }

    // Adding the view models to dependency injection
    viewModel { FrequentlyQuestionsViewModel(get()) }
    viewModel { LoginOtpViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { StudentHomeViewModel(get(), get()) }
    viewModel { ExamViewModel(get()) }
    viewModel { ExamsListViewModel(get()) }
    viewModel { SplashScreenViewModel(get(), get()) }
    viewModel { TeacherHomeViewModel(get()) }
    viewModel { AddExamQuestionViewModel(get()) }
    viewModel { ExamMainViewModel(get()) }
}