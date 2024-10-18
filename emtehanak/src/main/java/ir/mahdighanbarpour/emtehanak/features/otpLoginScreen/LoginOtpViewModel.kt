package ir.mahdighanbarpour.emtehanak.features.otpLoginScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.emtehanak.model.data.StudentMainResult
import ir.mahdighanbarpour.emtehanak.model.data.TeacherMainResult
import ir.mahdighanbarpour.emtehanak.model.repositories.StudentRepository
import ir.mahdighanbarpour.emtehanak.model.repositories.TeacherRepository

class LoginOtpViewModel(
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository
) : ViewModel() {

    // Determines whether something is being received from the server or not
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    // receiving student information
    fun loginStudent(phoneNumber: String): Single<StudentMainResult> {
        isDataLoading.onNext(true)

        return studentRepository.loginStudent(phoneNumber).doFinally {
            isDataLoading.onNext(false)
        }
    }

    // receiving teacher information
    fun loginTeacher(phoneNumber: String): Single<TeacherMainResult> {
        isDataLoading.onNext(true)

        return teacherRepository.loginTeacher(phoneNumber).doFinally {
            isDataLoading.onNext(false)
        }
    }
}