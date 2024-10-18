package ir.mahdighanbarpour.emtehanak.di

import android.content.Context
import ir.mahdighanbarpour.emtehanak.features.addExamQuestionScreen.AddExamQuestionViewModel
import ir.mahdighanbarpour.emtehanak.features.examDetailScreen.ExamViewModel
import ir.mahdighanbarpour.emtehanak.features.examsListScreen.ExamsListViewModel
import ir.mahdighanbarpour.emtehanak.features.frequentlyQuestionsScreen.FrequentlyQuestionsViewModel
import ir.mahdighanbarpour.emtehanak.features.homeStudentScreen.StudentHomeViewModel
import ir.mahdighanbarpour.emtehanak.features.homeTeacherScreen.TeacherHomeViewModel
import ir.mahdighanbarpour.emtehanak.features.mainExamScreen.ExamMainViewModel
import ir.mahdighanbarpour.emtehanak.features.mainRegScreen.RegisterViewModel
import ir.mahdighanbarpour.emtehanak.features.myExamsScreen.MyExamsViewModel
import ir.mahdighanbarpour.emtehanak.features.otpLoginScreen.LoginOtpViewModel
import ir.mahdighanbarpour.emtehanak.features.splashScreen.SplashScreenViewModel
import ir.mahdighanbarpour.emtehanak.model.dataBase.MainDataBase
import ir.mahdighanbarpour.emtehanak.model.net.apiServiceBuilder
import ir.mahdighanbarpour.emtehanak.model.repositories.ExamRepository
import ir.mahdighanbarpour.emtehanak.model.repositories.FrequentlyQuestionsRepository
import ir.mahdighanbarpour.emtehanak.model.repositories.LessonRepository
import ir.mahdighanbarpour.emtehanak.model.repositories.StudentRepository
import ir.mahdighanbarpour.emtehanak.model.repositories.TeacherRepository
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
    viewModel { StudentHomeViewModel(get(), get(), get()) }
    viewModel { ExamViewModel(get()) }
    viewModel { ExamsListViewModel(get()) }
    viewModel { SplashScreenViewModel(get(), get()) }
    viewModel { TeacherHomeViewModel(get(), get()) }
    viewModel { AddExamQuestionViewModel(get()) }
    viewModel { ExamMainViewModel(get()) }
    viewModel { MyExamsViewModel(get()) }
}