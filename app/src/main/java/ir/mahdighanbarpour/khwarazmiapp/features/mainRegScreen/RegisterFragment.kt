package ir.mahdighanbarpour.khwarazmiapp.features.mainRegScreen

import android.content.Intent
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
import com.google.android.material.textfield.TextInputLayout
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentRegisterBinding
import ir.mahdighanbarpour.khwarazmiapp.features.mainLoginScreen.LoginActivity
import ir.mahdighanbarpour.khwarazmiapp.features.mainStudentScreen.StudentMainActivity
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_REGISTER_FRAGMENT_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.TEACHER
import ir.mahdighanbarpour.khwarazmiapp.utils.changeBoxStrokeColor
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var navController: NavController
    private lateinit var selectedRole: String
    private lateinit var textInputLayouts: ArrayList<TextInputLayout>

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedRole = requireArguments().getString(SEND_SELECTED_ROLE_TO_REGISTER_FRAGMENT_KEY)!!

        navController = Navigation.findNavController(view)

        textInputLayouts = arrayListOf(
            binding.etLayoutFullNameReg,
            binding.etLayoutDayReg,
            binding.etLayoutMonthReg,
            binding.etLayoutYearReg
        )

        if (selectedRole == TEACHER) {
            mainColor = R.color.teacher_color
        } else if (selectedRole == STUDENT) {
            mainColor = R.color.student_color
        }

        setMainData()
        setGradeAdapter()
        setBirthdayMonthAdapter()
        setExpertiseAdapter()
    }

    private fun setMainData() {
        if (selectedRole == STUDENT) {
            setSelectedRoleColor("نام و نام خانوادگی دانش آموز")
        } else {
            setSelectedRoleColor("نام و نام خانوادگی همراه دبیر")
        }
    }

    private fun setBirthdayMonthAdapter() {
        val monthAdapter =
            ArrayAdapter(requireContext(), R.layout.layout_drop_down_items, monthItems)
        binding.tvMonthReg.setAdapter(monthAdapter)
    }

    private fun setGradeAdapter() {
        val gradeAdapter =
            ArrayAdapter(requireContext(), R.layout.layout_drop_down_items, gradeItems)
        binding.tvStudentGradeReg.setAdapter(gradeAdapter)
    }

    private fun setExpertiseAdapter() {
        val expertiseAdapter =
            ArrayAdapter(requireContext(), R.layout.layout_drop_down_items, expertiseItems)
        binding.tvTeacherExpertiseReg.setAdapter(expertiseAdapter)
    }

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

        if (enteredName.isEmpty()) {
            binding.etLayoutFullNameReg.error = "نام و نام خانوادگی خود را وارد کنید"
        } else if (enteredName.length <= 5) {
            binding.etLayoutFullNameReg.error = "نام و نام خانوادگی معتبر نمی باشد"
        } else {
            binding.etLayoutFullNameReg.isErrorEnabled = false
            isEnteredNameOk = true
        }

        if (checkEnteredDay(enteredDay)) {
            isEnteredDayOk = true
        }

        if (checkEnteredMonth(enteredDay, enteredMonth)) {
            isEnteredMonthOk = true
        }

        if (checkEnteredYear(enteredYear)) {
            isEnteredYearOk = true
        }

        if (selectedRole == TEACHER) {
            isGradeOk = true
            isTeacherDataOk = checkTeacherInputs()
        } else {
            isGradeOk = checkEnteredGrade(enteredGrade)
        }

        if (isEnteredDayOk && isEnteredMonthOk && isEnteredYearOk && isEnteredNameOk && isTeacherDataOk && isGradeOk) {
            if (selectedRole == TEACHER) {
                makeShortToast(
                    requireContext(), "بخش دبیران در حال توسعه است. با تشکر از شکیبایی شما"
                )
                // TODO
            } else {
                val intent = Intent(requireContext(), StudentMainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    private fun checkTeacherInputs(): Boolean {
        val enteredExpertise = binding.tvTeacherExpertiseReg.text.toString()
        val enteredActivityYear = binding.etActivityYearReg.text.toString()

        var isEnteredExpertiseOk = false
        var isEnteredActivityYearOk = false

        if (enteredExpertise.isEmpty()) {
            binding.etLayoutTeacherExpertiseReg.error = "رشته مورد تدریس خود را وارد کنید"
        } else if (enteredExpertise !in expertiseItems) {
            binding.etLayoutTeacherExpertiseReg.error = "رشته مورد تدریس معتبر نمی باشد"
        } else {
            binding.etLayoutTeacherExpertiseReg.isErrorEnabled = false
            isEnteredExpertiseOk = true
        }

        if (enteredActivityYear.isEmpty()) {
            binding.etLayoutTeacherActivityYearReg.error = "سال شروع به فعالیت خود را وارد کنید"
        } else if (enteredActivityYear.toInt() !in 1300..1402) {
            binding.etLayoutTeacherActivityYearReg.error = "سال شروع به فعالیت معتبر نمی باشد"
        } else {
            binding.etLayoutTeacherActivityYearReg.isErrorEnabled = false
            isEnteredActivityYearOk = true
        }

        return isEnteredExpertiseOk && isEnteredActivityYearOk
    }

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

        if (selectedRole == TEACHER) {
            binding.etLayoutStudentGradeReg.visibility = View.GONE

            binding.etLayoutTeacherExpertiseReg.visibility = View.VISIBLE
            binding.etLayoutTeacherActivityYearReg.visibility = View.VISIBLE
        }

        (requireActivity() as LoginActivity).changeAppColor(selectedRole)
    }
}