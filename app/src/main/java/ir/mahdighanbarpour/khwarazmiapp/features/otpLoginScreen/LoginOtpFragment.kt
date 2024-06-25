package ir.mahdighanbarpour.khwarazmiapp.features.otpLoginScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentLoginOtpBinding
import ir.mahdighanbarpour.khwarazmiapp.features.mainLoginScreen.LoginActivity
import ir.mahdighanbarpour.khwarazmiapp.features.mainStudentScreen.StudentMainActivity
import ir.mahdighanbarpour.khwarazmiapp.features.mainTeacherScreen.TeacherMainActivity
import ir.mahdighanbarpour.khwarazmiapp.model.data.StudentMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.TeacherMainResult
import ir.mahdighanbarpour.khwarazmiapp.utils.IS_USER_LOGGED_IN
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_ENTERED_PHONE_NUMBER_TO_OTP_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_ENTERED_PHONE_NUMBER_TO_REG_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_LOGIN_OTP_FRAGMENT_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_REGISTER_FRAGMENT_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.TEACHER
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_FULL_NAME
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_GRADE
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_PHONE_NUM
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_ROLE
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Timer
import java.util.TimerTask

class LoginOtpFragment : Fragment() {

    private lateinit var binding: FragmentLoginOtpBinding
    private lateinit var navController: NavController
    private lateinit var selectedRole: String
    private lateinit var enteredNumber: String
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var parentActivity: LoginActivity

    private var timer: Timer? = null

    private var editTextsList: ArrayList<EditText> = arrayListOf()

    private var secondEtIsDelClicked = false
    private var thirdEtIsDelClicked = false
    private var fourthEtIsDelClicked = false
    private var isLoading = false
    private var otpResendTime = 60

    private val loginOtpViewModel: LoginOtpViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

    private val sharedPreferences: SharedPreferences by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginOtpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        Toast.makeText(
            requireContext(),
            "به دلیل اینکه نرم افزار در نسخه های اولیه قرار دارد و امکانات آن کامل نشده است کد 1234 را وارد کنید.",
            Toast.LENGTH_SHORT
        ).show()

        initUi()
        setMainData()
        otpTimer()
        listener()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Cancel the code resend timer
        timer?.cancel()

        // Clear the values in Composite Disposable
        compositeDisposable.clear()
    }


    @SuppressLint("SetTextI18n")
    private fun listener() {
        // Codes related to OTP inputs {
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
            // Resend the code
            binding.tvResendOTP.text = "$otpResendTime ثانیه تا ارسال مجدد"
            binding.tvResendOTP.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))

            binding.ivResendOTP.visibility = View.GONE

            otpTimer()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initUi() {
        parentActivity = requireActivity() as LoginActivity

        // // Creating a list of inputs on the page
        editTextsList = arrayListOf(
            binding.etFirstOtp, binding.etSecondOtp, binding.etThirdOtp, binding.etFourthOtp
        )

        // Getting the phone number and the selected role
        selectedRole = requireArguments().getString(SEND_SELECTED_ROLE_TO_LOGIN_OTP_FRAGMENT_KEY)!!
        enteredNumber = requireArguments().getString(SEND_ENTERED_PHONE_NUMBER_TO_OTP_PAGE_KEY)!!

        binding.tvOTP.text = "کد فعالسازی 4 رقمی به $enteredNumber ارسال شد"

        editor = sharedPreferences.edit()

        // It changes the resend OTP color of the app based on the role sent
        binding.ivResendOTP.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                if (selectedRole == TEACHER) R.color.teacher_color else R.color.student_color
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    private fun setMainData() {
        // Checking what role the user has selected
        if (selectedRole == STUDENT) {
            changeEditTextsColor(R.color.student_color)
        } else {
            changeEditTextsColor(R.color.teacher_color)
        }
    }

    private fun otpTimer() {
        // Creating code resend countdown timer
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
        // Resend the code
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
        // Checking if the user has internet or not
        if (isInternetAvailable(requireContext())) {
            // Checking the OTP entered by the user
            val enteredOtpCode =
                binding.etFirstOtp.text.toString() + binding.etSecondOtp.text.toString() + binding.etThirdOtp.text.toString() + binding.etFourthOtp.text.toString()

            // Check the correctness of the entered code
            if (enteredOtpCode != "1234") {
                changeEditTextsColor(R.color.red)
                binding.tvOtpError.visibility = View.VISIBLE
            } else {
                if (!isLoading) {
                    if (selectedRole == STUDENT) {
                        playLoadingAnim()
                        getStudentInformation()
                    } else {
                        playLoadingAnim()
                        getTeacherInformation()
                    }
                }
            }
        } else {
            Snackbar.make(
                binding.root, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE
            ).setAction("تلاش مجدد") { checkOtpCode() }.show()
        }
    }

    private fun playLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(loginOtpViewModel.isDataLoading.subscribe {
            requireActivity().runOnUiThread {
                parentActivity.playPauseLoadingAnim(it)
                isLoading = it
            }
        })
    }

    private fun getStudentInformation() {
        // receiving student information
        loginOtpViewModel.loginStudent(enteredNumber).asyncRequest()
            .subscribe(object : SingleObserver<StudentMainResult> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Snackbar.make(
                        binding.root, "خطا! لطفا دوباره تلاش کنید", Snackbar.LENGTH_LONG
                    ).setAction("تلاش مجدد") { checkOtpCode() }.show()
                    Log.v("testLog", e.message.toString())
                }

                override fun onSuccess(t: StudentMainResult) {
                    checkStudentInformation(t)
                }
            })
    }

    private fun checkStudentInformation(result: StudentMainResult) {
        // Checking if there is a student with this phone number or not
        if (result.status == 200) {
            // Saves the student's login and then opens the student's home page
            editor.putBoolean(IS_USER_LOGGED_IN, true)
            editor.putString(USER_FULL_NAME, result.result.student.fullName)
            editor.putString(USER_PHONE_NUM, enteredNumber)
            editor.putString(USER_GRADE, result.result.student.grade)
            editor.putString(USER_ROLE, selectedRole)
            editor.commit()

            val intent = Intent(requireContext(), StudentMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            // Open the registration page
            changeEditTextsColor(R.color.student_color)

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

    private fun getTeacherInformation() {
        // receiving teacher information
        loginOtpViewModel.loginTeacher(enteredNumber).asyncRequest()
            .subscribe(object : SingleObserver<TeacherMainResult> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Snackbar.make(
                        binding.root, "خطا! لطفا دوباره تلاش کنید", Snackbar.LENGTH_LONG
                    ).setAction("تلاش مجدد") { checkOtpCode() }.show()
                    Log.v("testLog", e.message.toString())
                }

                override fun onSuccess(t: TeacherMainResult) {
                    checkTeacherInformation(t)
                }
            })
    }

    private fun checkTeacherInformation(result: TeacherMainResult) {
        // Checking if there is a teacher with this phone number or not
        if (result.status == 200) {
            // Saves the teacher's login and then opens the teacher's home page
            editor.putBoolean(IS_USER_LOGGED_IN, true)
            editor.putString(USER_FULL_NAME, result.result.teacher.fullName)
            editor.putString(USER_PHONE_NUM, enteredNumber)
            editor.putString(USER_GRADE, result.result.teacher.grades)
            editor.putString(USER_ROLE, selectedRole)
            editor.commit()

            val intent = Intent(requireContext(), TeacherMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            // Open the registration page
            changeEditTextsColor(R.color.teacher_color)

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
        // It changes the app color of the app based on the role sent
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