package ir.mahdighanbarpour.emtehanak.features.mainRegScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.emtehanak.model.data.StudentMainResult
import ir.mahdighanbarpour.emtehanak.model.data.TeacherMainResult
import ir.mahdighanbarpour.emtehanak.model.repositories.StudentRepository
import ir.mahdighanbarpour.emtehanak.model.repositories.TeacherRepository

class RegisterViewModel(
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository
) : ViewModel() {

    // Determines whether something is being received from the server or not
    val isDataLoading: BehaviorSubject<Boolean> = BehaviorSubject.create()

    // Student registration
    fun registerStudent(
        name: String, phoneNumber: String, birthday: String, grade: String
    ): Single<StudentMainResult> {
        // Determines whether something is being received from the server or not
        isDataLoading.onNext(true)

        // Student registration
        return studentRepository.registerStudent(name, phoneNumber, birthday, grade).doFinally {
            isDataLoading.onNext(false)
        }
    }

    // Teacher registration
    fun registerTeacher(
        name: String,
        phoneNumber: String,
        birthday: String,
        studyField: String,
        grade: String,
        activityYear: String
    ): Single<TeacherMainResult> {
        // Determines whether something is being received from the server or not
        isDataLoading.onNext(true)

        // Teacher registration
        return teacherRepository.registerTeacher(
            name, phoneNumber, birthday, studyField, grade, activityYear
        ).doFinally {
            isDataLoading.onNext(false)
        }
    }
}