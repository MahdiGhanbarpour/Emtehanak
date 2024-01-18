package ir.mahdighanbarpour.khwarazmiapp.features.otpLoginScreen

import android.annotation.SuppressLint
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentLoginOtpBinding
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_ENTERED_PHONE_NUMBER_TO_OTP_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_ENTERED_PHONE_NUMBER_TO_REG_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_LOGIN_OTP_FRAGMENT_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_REGISTER_FRAGMENT_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.TEACHER
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import java.util.Timer
import java.util.TimerTask

class LoginOtpFragment : Fragment() {

    private lateinit var binding: FragmentLoginOtpBinding
    private lateinit var navController: NavController
    private lateinit var selectedRole: String
    private lateinit var enteredNumber: String

    private var timer: Timer? = null

    private var editTextsList: ArrayList<EditText> = arrayListOf()

    private var secondEtIsDelClicked = false
    private var thirdEtIsDelClicked = false
    private var fourthEtIsDelClicked = false
    private var otpResendTime = 60

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginOtpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        initUi()
        setMainData()
        otpTimer()
        listener()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }


    @SuppressLint("SetTextI18n")
    private fun listener() {
        binding.etFirstOtp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.etSecondOtp.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
            }
        })

        binding.etSecondOtp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.etThirdOtp.requestFocus()
                }
                if (s.isEmpty()) {
                    binding.etFirstOtp.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
            }
        })

        binding.etThirdOtp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.etFourthOtp.requestFocus()
                }
                if (s.isEmpty()) {
                    binding.etSecondOtp.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
            }
        })

        binding.etFourthOtp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()) {
                    binding.etThirdOtp.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
            }
        })
        binding.etFirstOtp.setOnKeyListener { _, keyCode, _ ->
            if (keyCode != KeyEvent.KEYCODE_DEL) {
                if (binding.etFirstOtp.text.isEmpty()) {
                    return@setOnKeyListener false
                } else {
                    binding.etSecondOtp.requestFocus()
                    return@setOnKeyListener false
                }
            } else {
                return@setOnKeyListener false
            }
        }
        binding.etSecondOtp.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (binding.etSecondOtp.text.isEmpty()) {
                    secondEtIsDelClicked = if (secondEtIsDelClicked) {
                        binding.etFirstOtp.requestFocus()
                        false
                    } else {
                        true
                    }
                }
                return@setOnKeyListener false
            } else {
                if (binding.etSecondOtp.text.length != 1) {
                    return@setOnKeyListener false
                } else {
                    binding.etThirdOtp.requestFocus()
                    return@setOnKeyListener true
                }
            }
        }
        binding.etThirdOtp.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (binding.etThirdOtp.text.isEmpty()) {
                    thirdEtIsDelClicked = if (thirdEtIsDelClicked) {
                        binding.etSecondOtp.requestFocus()
                        false
                    } else {
                        true
                    }
                }
                return@setOnKeyListener false
            } else {
                if (binding.etThirdOtp.text.length != 1) {
                    return@setOnKeyListener false
                } else {
                    binding.etFourthOtp.requestFocus()
                    return@setOnKeyListener true
                }
            }
        }
        binding.etFourthOtp.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (binding.etFourthOtp.text.isEmpty()) {
                    fourthEtIsDelClicked = if (fourthEtIsDelClicked) {
                        binding.etThirdOtp.requestFocus()
                        false
                    } else {
                        true
                    }
                }
                return@setOnKeyListener false
            } else {
                return@setOnKeyListener false
            }
        }
        binding.ivResendOTP.setOnClickListener {
            binding.tvResendOTP.text = "$otpResendTime ثانیه تا ارسال مجدد"
            binding.tvResendOTP.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))

            binding.ivResendOTP.visibility = View.GONE

            otpTimer()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initUi() {
        editTextsList = arrayListOf(
            binding.etFirstOtp, binding.etSecondOtp, binding.etThirdOtp, binding.etFourthOtp
        )

        selectedRole = requireArguments().getString(SEND_SELECTED_ROLE_TO_LOGIN_OTP_FRAGMENT_KEY)!!

        enteredNumber = requireArguments().getString(SEND_ENTERED_PHONE_NUMBER_TO_OTP_PAGE_KEY)!!
        binding.tvOTP.text = "کد فعالسازی 4 رقمی به $enteredNumber ارسال شد"

        binding.ivResendOTP.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                if (selectedRole == TEACHER) R.color.teacher_color else R.color.student_color
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    private fun setMainData() {
        if (selectedRole == STUDENT) {
            changeEditTextsColor(R.color.student_color)
        } else {
            changeEditTextsColor(R.color.teacher_color)
        }
    }

    private fun otpTimer() {
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                requireActivity().runOnUiThread {
                    otpResendTime--
                    binding.tvResendOTP.text = "$otpResendTime ثانیه تا ارسال دوباره"

                    if (otpResendTime == 0) {
                        resendOTP()
                    }
                }
            }
        }, 1000, 1000)
    }

    private fun resendOTP() {
        timer!!.cancel()
        otpResendTime = 60

        binding.ivResendOTP.visibility = View.VISIBLE


        binding.tvResendOTP.text = "ارسال مجدد کد"
        binding.tvResendOTP.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (selectedRole == TEACHER) R.color.teacher_color else R.color.student_color
            )
        )
    }

    fun checkOtpCode() {
        val enteredOtpCode =
            binding.etFirstOtp.text.toString() + binding.etSecondOtp.text.toString() + binding.etThirdOtp.text.toString() + binding.etFourthOtp.text.toString()

        if (enteredOtpCode != "1234") {
            changeEditTextsColor(R.color.red)
            makeShortToast(requireContext(), "کد وارد شده معتبر نیست")
        } else {
            changeEditTextsColor(if (selectedRole == STUDENT) R.color.student_color else R.color.teacher_color)

            val roleBundle = Bundle()
            roleBundle.putString(
                SEND_SELECTED_ROLE_TO_REGISTER_FRAGMENT_KEY, selectedRole
            )

            roleBundle.putString(SEND_ENTERED_PHONE_NUMBER_TO_REG_PAGE_KEY, enteredNumber)

            navController.navigate(
                R.id.action_loginOtpFragment_to_registerFragment, roleBundle
            )
        }
    }

    private fun changeEditTextsColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            for (editTexts in editTextsList) {
                editTexts.background.colorFilter = BlendModeColorFilter(
                    ContextCompat.getColor(requireContext(), color), BlendMode.SRC_ATOP
                )
            }
        } else {
            for (editTexts in editTextsList) {
                editTexts.background.setColorFilter(
                    ContextCompat.getColor(requireContext(), color), PorterDuff.Mode.SRC_ATOP
                )
            }
        }
    }
}