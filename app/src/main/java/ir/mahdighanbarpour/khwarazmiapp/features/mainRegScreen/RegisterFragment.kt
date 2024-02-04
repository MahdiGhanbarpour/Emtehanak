package ir.mahdighanbarpour.khwarazmiapp.features.mainRegScreen

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentRegisterBinding
import ir.mahdighanbarpour.khwarazmiapp.features.mainLoginScreen.LoginActivity
import ir.mahdighanbarpour.khwarazmiapp.features.mainStudentScreen.StudentMainActivity
import ir.mahdighanbarpour.khwarazmiapp.model.data.MainResult
import ir.mahdighanbarpour.khwarazmiapp.utils.IS_USER_LOGGED_IN
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_ENTERED_PHONE_NUMBER_TO_REG_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_REGISTER_FRAGMENT_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.TEACHER
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_FULL_NAME
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_GRADE
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_PHONE_NUM
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_ROLE
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.changeBoxStrokeColor
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var navController: NavController
    private lateinit var selectedRole: String
    private lateinit var enteredPhoneNum: String
    private lateinit var textInputLayouts: ArrayList<TextInputLayout>
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var parentActivity: LoginActivity

    private var mainColor: Int = 0

    private val monthItems = listOf(
        "فروردین",
        "اردیبهشت",
        "خرداد",
        "تیر",
        "مرداد",
        "شهریور",
        "مهر",
        "آبان",
        "آذر",
        "دی",
        "بهمن",
        "اسفند"
    )
    private val expertiseItems = listOf(
        "علوم تجربی",
        "ریاضی",
        "ادبیات فارسی",
        "زبان انگلیسی",
        "دینی",
        "آمادگی دفاعی",
        "زبان عربی",
        "مطالعات اجتماعی",
        "هنر",
        "کار و فناوری"
    )
    private val gradeItems = listOf(
        "اول",
        "دوم",
        "سوم",
        "چهارم",
        "پنجم",
        "ششم",
        "هفتم",
        "هشتم",
        "نهم",
        "دهم",
        "یازدهم",
        "دوازدهم"
    )

    private val registerViewModel: RegisterViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

    private val sharedPreferences: SharedPreferences by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        setGradeAdapter()
        setBirthdayMonthAdapter()
        setExpertiseAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Clear the values in Composite Disposable
        compositeDisposable.clear()
    }


    private fun initUi() {
        parentActivity = requireActivity() as LoginActivity

        // Getting the phone number and the selected role
        selectedRole = requireArguments().getString(SEND_SELECTED_ROLE_TO_REGISTER_FRAGMENT_KEY)!!
        enteredPhoneNum = requireArguments().getString(SEND_ENTERED_PHONE_NUMBER_TO_REG_PAGE_KEY)!!

        navController = Navigation.findNavController(requireView())

        // Creating a list of inputs on the page
        textInputLayouts = arrayListOf(
            binding.etLayoutFullNameReg,
            binding.etLayoutDayReg,
            binding.etLayoutMonthReg,
            binding.etLayoutYearReg
        )

        // Checking what role the user has selected
        if (selectedRole == TEACHER) {
            mainColor = R.color.teacher_color
        } else if (selectedRole == STUDENT) {
            mainColor = R.color.student_color
        }

        editor = sharedPreferences.edit()


        if (selectedRole == STUDENT) {
            setSelectedRoleColor("نام و نام خانوادگی دانش آموز")
        } else {
            setSelectedRoleColor("نام و نام خانوادگی همراه دبیر")
        }
    }

    // Setting the dropdown data of the birth month
    private fun setBirthdayMonthAdapter() {
        val monthAdapter =
            ArrayAdapter(requireContext(), R.layout.layout_drop_down_items, monthItems)
        binding.tvMonthReg.setAdapter(monthAdapter)
    }

    // Setting the dropdown data of the grade
    private fun setGradeAdapter() {
        val gradeAdapter =
            ArrayAdapter(requireContext(), R.layout.layout_drop_down_items, gradeItems)
        binding.tvStudentGradeReg.setAdapter(gradeAdapter)
    }

    // Setting the dropdown data of the expertise
    private fun setExpertiseAdapter() {
        val expertiseAdapter =
            ArrayAdapter(requireContext(), R.layout.layout_drop_down_items, expertiseItems)
        binding.tvTeacherExpertiseReg.setAdapter(expertiseAdapter)
    }

    // Checking the information entered by the user
    fun checkInputs() {
        val enteredName = binding.etFullNameReg.text.toString()
        val enteredDay = binding.etDayReg.text.toString()
        val enteredMonth = binding.tvMonthReg.text.toString()
        val enteredYear = binding.etYearReg.text.toString()
        val enteredGrade = binding.tvStudentGradeReg.text.toString()

        var isEnteredDayOk = false
        var isEnteredMonthOk = false
        var isEnteredYearOk = false
        var isEnteredNameOk = false
        var isTeacherDataOk = selectedRole == STUDENT

        val isGradeOk: Boolean

        // Is the entered name correct ?
        if (enteredName.isEmpty()) {
            binding.etLayoutFullNameReg.error = "نام و نام خانوادگی خود را وارد کنید"
        } else if (enteredName.length <= 5) {
            binding.etLayoutFullNameReg.error = "نام و نام خانوادگی معتبر نمی باشد"
        } else {
            binding.etLayoutFullNameReg.isErrorEnabled = false
            isEnteredNameOk = true
        }

        // Is the date of birth correct ?
        if (checkEnteredDay(enteredDay)) {
            isEnteredDayOk = true
        }

        // Is the date of birth correct ?
        if (checkEnteredMonth(enteredDay, enteredMonth)) {
            isEnteredMonthOk = true
        }

        // Is the date of birth correct ?
        if (checkEnteredYear(enteredYear)) {
            isEnteredYearOk = true
        }

        // If the user is a student, it checks his grade; otherwise, it checks the teacher data
        if (selectedRole == TEACHER) {
            isGradeOk = true
            isTeacherDataOk = checkTeacherInputs()
        } else {
            isGradeOk = checkEnteredGrade(enteredGrade)
        }

        // Is all the information correct ?
        if (isEnteredDayOk && isEnteredMonthOk && isEnteredYearOk && isEnteredNameOk && isTeacherDataOk && isGradeOk) {
            // Checking the user role and opening the corresponding home page
            if (selectedRole == TEACHER) {
                makeShortToast(
                    requireContext(), "بخش دبیران در حال توسعه است. با تشکر از شکیبایی شما"
                )
                // TODO
            } else {
                if (isInternetAvailable(requireContext())) {
                    playLoadingAnim()

                    registerStudent(
                        enteredName,
                        enteredPhoneNum,
                        "$enteredYear/${monthItems.indexOf(enteredMonth)}/$enteredDay",
                        enteredGrade
                    )
                } else {
                    Snackbar.make(
                        binding.root, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE
                    ).setAction("تلاش مجدد") { checkInputs() }.show()
                }
            }
        }
    }

    // Checking the information entered by the teacher
    private fun checkTeacherInputs(): Boolean {
        val enteredExpertise = binding.tvTeacherExpertiseReg.text.toString()
        val enteredActivityYear = binding.etActivityYearReg.text.toString()

        var isEnteredExpertiseOk = false
        var isEnteredActivityYearOk = false

        // Is the entered expertise correct ?
        if (enteredExpertise.isEmpty()) {
            binding.etLayoutTeacherExpertiseReg.error = "رشته مورد تدریس خود را وارد کنید"
        } else if (enteredExpertise !in expertiseItems) {
            binding.etLayoutTeacherExpertiseReg.error = "رشته مورد تدریس معتبر نمی باشد"
        } else {
            binding.etLayoutTeacherExpertiseReg.isErrorEnabled = false
            isEnteredExpertiseOk = true
        }

        // Is the entered year of activity correct?
        if (enteredActivityYear.isEmpty()) {
            binding.etLayoutTeacherActivityYearReg.error = "سال شروع به فعالیت خود را وارد کنید"
        } else if (enteredActivityYear.toInt() !in 1300..1402) {
            binding.etLayoutTeacherActivityYearReg.error = "سال شروع به فعالیت معتبر نمی باشد"
        } else {
            binding.etLayoutTeacherActivityYearReg.isErrorEnabled = false
            isEnteredActivityYearOk = true
        }

        // Are both correct ?
        return isEnteredExpertiseOk && isEnteredActivityYearOk
    }

    // Is the date of birth correct ?
    private fun checkEnteredDay(day: String): Boolean {
        return if (day.isEmpty()) {
            binding.etLayoutDayReg.error = " "
            makeShortToast(requireContext(), "روز تولد خود را وارد کنید")

            false
        } else if (day.toInt() !in 1..31) {
            binding.etLayoutDayReg.error = " "
            makeShortToast(requireContext(), "روز تولد وارد شده معتبر نیست")

            false
        } else {
            binding.etLayoutDayReg.isErrorEnabled = false
            true
        }
    }

    // Is the date of birth correct ?
    private fun checkEnteredMonth(day: String, month: String): Boolean {
        return if (month.isEmpty()) {
            binding.etLayoutMonthReg.error = " "
            makeShortToast(requireContext(), "ماه تولد خود را وارد کنید")

            false
        } else if (month !in monthItems) {
            binding.etLayoutMonthReg.error = " "
            makeShortToast(requireContext(), "ماه تولد وارد شده معتبر نیست")

            false
        } else {
            if (day.isNotEmpty()) {
                binding.etLayoutMonthReg.isErrorEnabled = false
                checkEnteredDayByMonth(day, month)
            } else {
                binding.etLayoutMonthReg.isErrorEnabled = false

                binding.etLayoutDayReg.error = " "
                makeShortToast(requireContext(), "روز تولد خود را وارد کنید")

                false
            }
        }
    }

    // Does the entered birth date match the entered birth month?
    private fun checkEnteredDayByMonth(day: String, month: String): Boolean {
        return if (month in arrayListOf(
                "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور"
            ) && day.toInt() > 31
        ) {
            binding.etLayoutDayReg.error = " "
            makeShortToast(requireContext(), "روز تولد وارد شده معتبر نیست")

            false
        } else if (month in arrayListOf(
                "بهمن", "دی", "آذر", "آبان", "مهر", "اسفند"
            ) && day.toInt() > 30
        ) {
            binding.etLayoutDayReg.error = " "
            makeShortToast(requireContext(), "روز تولد وارد شده معتبر نیست")

            false
        } else {
            binding.etLayoutDayReg.isErrorEnabled = false
            true
        }
    }

    // Is the date of birth correct ?
    private fun checkEnteredYear(year: String): Boolean {
        return if (year.isEmpty()) {
            binding.etLayoutYearReg.error = " "
            makeShortToast(requireContext(), "سال تولد خود را وارد کنید")

            false
        } else if (year.toInt() !in 1300..1402) {
            binding.etLayoutYearReg.error = " "
            makeShortToast(requireContext(), "سال تولد وارد شده معتبر نیست")

            false
        } else {
            binding.etLayoutYearReg.isErrorEnabled = false
            true
        }
    }

    // Is the entered grade correct?
    private fun checkEnteredGrade(grade: String): Boolean {
        return if (grade.isEmpty()) {
            binding.etLayoutStudentGradeReg.error = "پایه تحصیلی خود را وارد کنید"

            false
        } else if (grade !in gradeItems) {
            binding.etLayoutStudentGradeReg.error = "پایه تحصیلی معتبر نمی باشد"

            false
        } else {
            binding.etLayoutStudentGradeReg.isErrorEnabled = false

            true
        }
    }

    private fun registerStudent(
        name: String, phoneNumber: String, birthday: String, grade: String
    ) {
        // receiving information
        registerViewModel.registerStudent(name, phoneNumber, birthday, grade).asyncRequest()
            .subscribe(object : SingleObserver<MainResult> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Snackbar.make(
                        binding.root, "خطا! لطفا دوباره تلاش کنید", Snackbar.LENGTH_INDEFINITE
                    ).setAction("تلاش مجدد") { checkInputs() }.show()
                }

                override fun onSuccess(t: MainResult) {
                    if (t.status == 200) {
                        openStudentHomePage(name, grade)
                    }
                }
            })
    }

    private fun playLoadingAnim() {
        compositeDisposable.add(registerViewModel.isDataLoading.subscribe {
            requireActivity().runOnUiThread {
                parentActivity.playPauseLoadingAnim(it)
            }
        })
    }

    private fun openStudentHomePage(name: String, grade: String) {
        // Saves the student's login and then opens the student's home page
        editor.putBoolean(IS_USER_LOGGED_IN, true)
        editor.putString(USER_FULL_NAME, name)
        editor.putString(USER_PHONE_NUM, enteredPhoneNum)
        editor.putString(USER_GRADE, grade)
        editor.putString(USER_ROLE, selectedRole)
        editor.commit()

        val intent = Intent(requireContext(), StudentMainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    // It changes the color of the app based on the role sent
    private fun setSelectedRoleColor(
        hintText: String
    ) {
        binding.etLayoutFullNameReg.hint = hintText

        val colorStateList = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_focused), intArrayOf()), intArrayOf(
                getColor(requireContext(), mainColor), getColor(requireContext(), R.color.gray)
            )
        )

        for (textInputLayout in textInputLayouts) {
            changeBoxStrokeColor(requireContext(), textInputLayout, mainColor)
            textInputLayout.defaultHintTextColor = colorStateList
        }

        // Based on the role of the user, it shows the relevant inputs
        if (selectedRole == TEACHER) {
            binding.etLayoutStudentGradeReg.visibility = View.GONE

            binding.etLayoutTeacherExpertiseReg.visibility = View.VISIBLE
            binding.etLayoutTeacherActivityYearReg.visibility = View.VISIBLE
        }

        (requireActivity() as LoginActivity).changeAppColor(selectedRole)
    }
}