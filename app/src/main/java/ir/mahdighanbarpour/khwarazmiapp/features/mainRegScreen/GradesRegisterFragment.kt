package ir.mahdighanbarpour.khwarazmiapp.features.mainRegScreen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentGradesRegisterBinding
import ir.mahdighanbarpour.khwarazmiapp.features.mainTeacherScreen.TeacherMainActivity
import ir.mahdighanbarpour.khwarazmiapp.model.data.TeacherMainResult
import ir.mahdighanbarpour.khwarazmiapp.utils.IS_USER_LOGGED_IN
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_ENTERED_ACTIVITY_YEAR_TO_GRADES_REGISTER_FRAGMENT_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_ENTERED_BIRTHDAY_TO_GRADES_REGISTER_FRAGMENT_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_ENTERED_NAME_TO_GRADES_REGISTER_FRAGMENT_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_ENTERED_PHONE_NUMBER_TO_GRADES_REGISTER_FRAGMENT_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_ENTERED_STUDY_FIELD_TO_GRADES_REGISTER_FRAGMENT_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.TEACHER
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_FULL_NAME
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_GRADE
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_PHONE_NUM
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_ROLE
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_STUDY_FIELD
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class GradesRegisterFragment : Fragment() {

    private lateinit var binding: FragmentGradesRegisterBinding
    private lateinit var checkBoxes: List<MaterialCheckBox>
    private lateinit var editor: SharedPreferences.Editor

    private val registerViewModel: RegisterViewModel by viewModel()
    private val sharedPreferences: SharedPreferences by inject()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentGradesRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the checkBoxes list
        checkBoxes = listOf(
            binding.cbFirstGradesRegister,
            binding.cbSecondGradesRegister,
            binding.cbThirdGradesRegister,
            binding.cbFourthGradesRegister,
            binding.cbFifthGradesRegister,
            binding.cbSixthGradesRegister,
            binding.cbSeventhGradesRegister,
            binding.cbEighthGradesRegister,
            binding.cbNinthGradesRegister,
            binding.cbTenthGradesRegister,
            binding.cbEleventhGradesRegister,
            binding.cbTwelfthGradesRegister
        )

        // Initialize the SharedPreferences editor
        editor = sharedPreferences.edit()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Clear the compositeDisposable
        compositeDisposable.clear()
    }

    fun checkInputs() {
        // Checking if the user has selected a grade or not
        val selectedGrades = arrayListOf<String>()
        for (checkBox in checkBoxes) {
            // If the checkbox is checked, add it to the selectedGrades list
            if (checkBox.isChecked) {
                selectedGrades.add(checkBox.text.toString())
            }
        }

        // If the user has not selected a grade, show a toast message
        if (selectedGrades.isEmpty()) {
            makeShortToast(requireContext(), "حداقل یک پایه را انتخاب کنید")
        } else {
            getTeacherData(selectedGrades)
        }
    }

    private fun getTeacherData(selectedGrades: ArrayList<String>) {
        // Getting the data of the teacher
        val teacherName =
            requireArguments().getString(SEND_ENTERED_NAME_TO_GRADES_REGISTER_FRAGMENT_KEY)!!
        val teacherPhoneNumber =
            requireArguments().getString(SEND_ENTERED_PHONE_NUMBER_TO_GRADES_REGISTER_FRAGMENT_KEY)!!
        val teacherBirthday =
            requireArguments().getString(SEND_ENTERED_BIRTHDAY_TO_GRADES_REGISTER_FRAGMENT_KEY)!!
        val teacherStudyField =
            requireArguments().getString(SEND_ENTERED_STUDY_FIELD_TO_GRADES_REGISTER_FRAGMENT_KEY)!!
        val teacherActivityYear =
            requireArguments().getString(SEND_ENTERED_ACTIVITY_YEAR_TO_GRADES_REGISTER_FRAGMENT_KEY)!!

        // Converting the selectedGrades list to a JSON string
        val jsonGrades = Gson().toJson(selectedGrades)

        // Registering the teacher
        registerTeacher(
            teacherName,
            teacherPhoneNumber,
            teacherBirthday,
            teacherStudyField,
            jsonGrades,
            teacherActivityYear
        )
    }

    // Registering the teacher
    private fun registerTeacher(
        name: String,
        phoneNumber: String,
        birthday: String,
        studyField: String,
        grade: String,
        activityYear: String
    ) {
        // Registering the teacher
        registerViewModel.registerTeacher(
            name, phoneNumber, birthday, studyField, grade, activityYear
        ).asyncRequest().subscribe(object : SingleObserver<TeacherMainResult> {
            override fun onSubscribe(d: Disposable) {
                // Adding the disposable to the compositeDisposable
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable) {
                // If there is an error, show a toast message
                Snackbar.make(
                    binding.root, "خطا! لطفا دوباره تلاش کنید", Snackbar.LENGTH_LONG
                ).setAction("تلاش مجدد") { checkInputs() }.show()
            }

            override fun onSuccess(t: TeacherMainResult) {
                // Checking whether the registration was successful or not
                if (t.status == 200) {
                    FirebaseAnalytics.getInstance(requireContext())
                        .logEvent(FirebaseAnalytics.Event.SIGN_UP, null)
                    openTeacherHomePage(name, phoneNumber, grade, studyField)
                } else {
                    Snackbar.make(
                        binding.root, "خطا! لطفا دوباره تلاش کنید", Snackbar.LENGTH_LONG
                    ).setAction("تلاش مجدد") { checkInputs() }.show()
                }
            }
        })
    }

    private fun openTeacherHomePage(
        name: String, phoneNumber: String, grade: String, studyField: String
    ) {
        // Saves the teacher's login and then opens the teacher's home page
        editor.putBoolean(IS_USER_LOGGED_IN, true)
        editor.putString(USER_FULL_NAME, name)
        editor.putString(USER_PHONE_NUM, phoneNumber)
        editor.putString(USER_GRADE, grade)
        editor.putString(USER_ROLE, TEACHER)
        editor.putString(USER_STUDY_FIELD, studyField)
        editor.commit()

        // Opens the teacher's home page
        val intent = Intent(requireContext(), TeacherMainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}