package ir.mahdighanbarpour.khwarazmiapp.features.splashScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.StudentMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.TeacherMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.StudentRepository
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.TeacherRepository

class SplashScreenViewModel(
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository
) : ViewModel() {

    fun loginStudent(phoneNumber: String): Single<StudentMainResult> {
        return studentRepository.loginStudent(phoneNumber)
    }

    fun loginTeacher(phoneNumber: String): Single<TeacherMainResult> {
        return teacherRepository.loginTeacher(phoneNumber)
    }
}