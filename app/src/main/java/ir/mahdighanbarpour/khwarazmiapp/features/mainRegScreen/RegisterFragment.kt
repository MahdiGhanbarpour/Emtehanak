package ir.mahdighanbarpour.khwarazmiapp.features.mainRegScreen

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentRegisterBinding
import ir.mahdighanbarpour.khwarazmiapp.features.loginScreen.LoginActivity
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_REGISTER_FRAGMENT_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.TEACHER
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var navController: NavController
    private lateinit var selectedRole: String

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

        if (selectedRole == TEACHER) {
            mainColor = R.color.teacher_color
        } else if (selectedRole == STUDENT) {
            mainColor = R.color.blue
        }

        setMainData()
        setBirthdayMonthAdapter()
        listener()
    }

    private fun setMainData() {
        if (selectedRole == STUDENT) {
            binding.radioGroupRoleReg.radioBtStudent.isChecked = true

            setSelectedRoleColor(
                "نام و نام خانوادگی دانش آموز",
                R.color.blue,
                R.color.white,
                R.drawable.shape_role_radio_button_background,
                R.drawable.selector_role_radio_button_background
            )
        } else {
            binding.radioGroupRoleReg.radioBtTeacher.isChecked = true

            setSelectedRoleColor(
                "نام و نام خانوادگی همراه دبیر",
                R.color.white,
                R.color.teacher_color,
                R.drawable.shape_role_radio_button_background_teacher,
                R.drawable.selector_role_radio_button_background_teacher
            )
        }
    }

    private fun listener() {
        binding.radioGroupRoleReg.radioGroupRole.setOnCheckedChangeListener { _, id ->

            // Change the Input Layouts hint based on the user's selected role
            when (id) {
                R.id.radioBtStudent -> {
                    mainColor = R.color.blue
                    selectedRole = STUDENT

                    setSelectedRoleColor(
                        "نام و نام خانوادگی دانش آموز",
                        R.color.blue,
                        R.color.white,
                        R.drawable.shape_role_radio_button_background,
                        R.drawable.selector_role_radio_button_background
                    )
                }

                R.id.radioBtTeacher -> {
                    mainColor = R.color.teacher_color
                    selectedRole = TEACHER

                    setSelectedRoleColor(
                        "نام و نام خانوادگی دبیر",
                        R.color.white,
                        R.color.teacher_color,
                        R.drawable.shape_role_radio_button_background_teacher,
                        R.drawable.selector_role_radio_button_background_teacher
                    )
                }

                else -> {
                    mainColor = R.color.blue
                    selectedRole = STUDENT

                    setSelectedRoleColor(
                        "نام و نام خانوادگی دانش آموز",
                        R.color.blue,
                        R.color.white,
                        R.drawable.shape_role_radio_button_background,
                        R.drawable.selector_role_radio_button_background
                    )
                }
            }
        }
    }

    private fun setBirthdayMonthAdapter() {
        val monthAdapter =
            ArrayAdapter(requireContext(), R.layout.layout_drop_down_items, monthItems)
        binding.tvMonthReg.setAdapter(monthAdapter)
    }

    fun checkInputs() {
        val enteredName = binding.etFullNameReg.text.toString()
        val enteredDay = binding.etDayReg.text.toString()
        val enteredMonth = binding.tvMonthReg.text.toString()
        val enteredYear = binding.etYearReg.text.toString()

        var isEnteredDayOk = false
        var isEnteredMonthOk = false
        var isEnteredYearOk = false
        var isEnteredNameOk = false

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

        if (isEnteredDayOk && isEnteredMonthOk && isEnteredYearOk && isEnteredNameOk) {
            if (selectedRole == TEACHER) {
                navController.navigate(R.id.action_registerFragment_to_teacherSpecFragment)
            } else {
                makeShortToast(requireContext(), "همه اطلاعات صحیح است")
            }
        }
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
            binding.etLayoutMonthReg.isErrorEnabled = false
            checkEnteredDayByMonth(day, month)
        }
    }

    private fun checkEnteredDayByMonth(day: String, month: String): Boolean {
        return when (month) {
            "اسفند" -> {
                if (day.toInt() > 30) {
                    binding.etLayoutDayReg.error = " "
                    makeShortToast(requireContext(), "روز تولد وارد شده معتبر نیست")
                    false
                } else {
                    true
                }
            }


            else -> true
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

    private fun setSelectedRoleColor(
        hintText: String, btTeacherColor: Int, btStudentColor: Int, shapeBG: Int, selectorBG: Int
    ) {

        binding.etLayoutFullNameReg.hint = hintText

        binding.etLayoutFullNameReg.boxStrokeColor = getColor(requireContext(), mainColor)

        binding.etLayoutDayReg.boxStrokeColor = getColor(requireContext(), mainColor)

        binding.etLayoutMonthReg.boxStrokeColor = getColor(requireContext(), mainColor)

        binding.etLayoutYearReg.boxStrokeColor = getColor(requireContext(), mainColor)

        binding.radioGroupRoleReg.radioGroupRole.background = ContextCompat.getDrawable(
            requireContext(), shapeBG
        )

        binding.radioGroupRoleReg.radioBtTeacher.background = ContextCompat.getDrawable(
            requireContext(), selectorBG
        )
        binding.radioGroupRoleReg.radioBtStudent.background = ContextCompat.getDrawable(
            requireContext(), selectorBG
        )


        binding.radioGroupRoleReg.radioBtStudent.setTextColor(
            getColor(
                requireContext(), btStudentColor
            )
        )
        binding.radioGroupRoleReg.radioBtTeacher.setTextColor(
            getColor(
                requireContext(), btTeacherColor
            )
        )

        val colorStateList = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_focused), intArrayOf()), intArrayOf(
                getColor(requireContext(), mainColor), getColor(requireContext(), R.color.gray)
            )
        )

        binding.etLayoutDayReg.defaultHintTextColor = colorStateList
        binding.etLayoutMonthReg.defaultHintTextColor = colorStateList
        binding.etLayoutYearReg.defaultHintTextColor = colorStateList
        binding.etLayoutFullNameReg.defaultHintTextColor = colorStateList

        (requireActivity() as LoginActivity).changeAppColor(selectedRole)
    }
}