package ir.mahdighanbarpour.emtehanak.features.splashScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.emtehanak.model.data.StudentMainResult
import ir.mahdighanbarpour.emtehanak.model.data.TeacherMainResult
import ir.mahdighanbarpour.emtehanak.model.repositories.StudentRepository
import ir.mahdighanbarpour.emtehanak.model.repositories.TeacherRepository

class SplashScreenViewModel(
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository
) : ViewModel() {

    fun loginStudent(phoneNumber: String): Single<StudentMainResult> {
        // Login student
        return studentRepository.loginStudent(phoneNumber)
    }

    fun loginTeacher(phoneNumber: String): Single<TeacherMainResult> {
        // Login teacher
        return teacherRepository.loginTeacher(phoneNumber)
    }
}