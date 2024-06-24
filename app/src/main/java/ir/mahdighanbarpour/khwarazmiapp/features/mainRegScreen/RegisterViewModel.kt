package ir.mahdighanbarpour.khwarazmiapp.features.mainRegScreen

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ir.mahdighanbarpour.khwarazmiapp.model.data.StudentMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.TeacherMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.StudentRepository
import ir.mahdighanbarpour.khwarazmiapp.model.repositories.TeacherRepository

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
        isDataLoading.onNext(true)

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
        isDataLoading.onNext(true)

        return teacherRepository.registerTeacher(
            name, phoneNumber, birthday, studyField, grade, activityYear
        ).doFinally {
            isDataLoading.onNext(false)
        }
    }
}