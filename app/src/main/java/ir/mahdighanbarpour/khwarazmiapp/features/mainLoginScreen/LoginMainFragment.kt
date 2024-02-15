package ir.mahdighanbarpour.khwarazmiapp.features.mainLoginScreen

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColorStateList
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentLoginMainBinding
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_ENTERED_PHONE_NUMBER_TO_OTP_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_LOGIN_OTP_FRAGMENT_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.TEACHER

class LoginMainFragment : Fragment() {

    private lateinit var binding: FragmentLoginMainBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Apply focus on the Text Input Layout
        binding.etLayoutNumLogin.requestFocus()

        navController = Navigation.findNavController(view)

        setSelectedRoleColor(
            STUDENT,
            "شماره تلفن همراه دانش آموز",
            R.color.student_color,
            R.color.student_color,
            R.color.white,
            R.drawable.shape_role_radio_button_background,
            R.drawable.selector_role_radio_button_background
        )
        binding.radioGroupRoleLogin.check(R.id.radioBtStudentLogin)

        // TODO
        Toast.makeText(
            requireContext(),
            "به دلیل اینکه نرم افزار در نسخه های اولیه قرار دارد و امکانات آن کامل نشده است برای استفاده بهتر از نرم افزار ترجیحا دانش آموز را انتخاب کنید",
            Toast.LENGTH_LONG
        ).show()

        listener()
    }

    private fun listener() {
        binding.radioGroupRoleLogin.setOnCheckedChangeListener { _, id ->

            // Based on the selected role, it makes changes in the color and other features of the page
            when (id) {
                R.id.radioBtStudentLogin -> {
                    setSelectedRoleColor(
                        STUDENT,
                        "شماره تلفن همراه دانش آموز",
                        R.color.student_color,
                        R.color.student_color,
                        R.color.white,
                        R.drawable.shape_role_radio_button_background,
                        R.drawable.selector_role_radio_button_background
                    )
                }

                R.id.radioBtTeacherLogin -> {
                    setSelectedRoleColor(
                        TEACHER,
                        "شماره تلفن همراه دبیر",
                        R.color.teacher_color,
                        R.color.white,
                        R.color.teacher_color,
                        R.drawable.shape_role_radio_button_background_teacher,
                        R.drawable.selector_role_radio_button_background_teacher
                    )
                }

                else -> {
                    setSelectedRoleColor(
                        STUDENT,
                        "شماره تلفن همراه دانش آموز",
                        R.color.student_color,
                        R.color.student_color,
                        R.color.white,
                        R.drawable.shape_role_radio_button_background,
                        R.drawable.selector_role_radio_button_background
                    )
                }
            }
        }
    }

    // Checking the validity of the entered phone number
    fun checkInput() {
        val enteredNum = binding.etNumLogin.text.toString()

        if (enteredNum.length != 11) {
            binding.etLayoutNumLogin.error = "شماره تلفن همراه معتبر نیست"
        } else if (!enteredNum.startsWith("09")) {
            binding.etLayoutNumLogin.error = "شماره تلفن همراه می‌بایست با 09 اغاز شود"
        } else if (!TextUtils.isDigitsOnly(enteredNum)) {
            binding.etLayoutNumLogin.error = "شماره تلفن همراه معتبر نیست"
        } else {
            val bundle = Bundle()

            bundle.putString(
                SEND_SELECTED_ROLE_TO_LOGIN_OTP_FRAGMENT_KEY,
                if (binding.radioBtStudentLogin.isChecked) STUDENT else TEACHER
            )

            bundle.putString(
                SEND_ENTERED_PHONE_NUMBER_TO_OTP_PAGE_KEY, enteredNum
            )

            // Opening the OTP page and send the role and phone number to it
            navController.navigate(
                R.id.action_loginMainFragment_to_loginOtpFragment, bundle
            )

            binding.etLayoutNumLogin.error = null
        }
    }

    // Based on the selected role, it makes changes in the color and other features of the page
    private fun setSelectedRoleColor(
        selectedRole: String,
        hintText: String,
        mainColor: Int,
        btTeacherColor: Int,
        btStudentColor: Int,
        shapeBG: Int,
        selectorBG: Int
    ) {
        binding.etLayoutNumLogin.hint = hintText

        binding.etLayoutNumLogin.boxStrokeColor =
            ContextCompat.getColor(requireContext(), mainColor)

        // Checking the user's Android version
        if (Build.VERSION.SDK_INT >= 23) {
            binding.etLayoutNumLogin.defaultHintTextColor =
                resources.getColorStateList(mainColor, requireActivity().theme)
        } else {
            binding.etLayoutNumLogin.defaultHintTextColor =
                getColorStateList(requireContext(), mainColor)
        }


        binding.radioGroupRoleLogin.background = ContextCompat.getDrawable(
            requireContext(), shapeBG
        )
        binding.radioBtTeacherLogin.background = ContextCompat.getDrawable(
            requireContext(), selectorBG
        )
        binding.radioBtStudentLogin.background = ContextCompat.getDrawable(
            requireContext(), selectorBG
        )


        binding.radioBtStudentLogin.setTextColor(
            ContextCompat.getColor(
                requireContext(), btStudentColor
            )
        )
        binding.radioBtTeacherLogin.setTextColor(
            ContextCompat.getColor(
                requireContext(), btTeacherColor
            )
        )

        // Notifying the parent activity
        (requireActivity() as LoginActivity).changeAppColor(selectedRole)
    }
}