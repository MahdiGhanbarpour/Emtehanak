package ir.mahdighanbarpour.khwarazmiapp.features.mainLoginScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityLoginBinding
import ir.mahdighanbarpour.khwarazmiapp.features.mainRegScreen.RegisterFragment
import ir.mahdighanbarpour.khwarazmiapp.features.otpLoginScreen.LoginOtpFragment
import ir.mahdighanbarpour.khwarazmiapp.features.termsScreen.TermsActivity
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.HelpBottomSheet
import ir.mahdighanbarpour.khwarazmiapp.utils.LOGIN_MAIN
import ir.mahdighanbarpour.khwarazmiapp.utils.LOGIN_OTP
import ir.mahdighanbarpour.khwarazmiapp.utils.REGISTER_MAIN
import ir.mahdighanbarpour.khwarazmiapp.utils.REGISTER_TEACHER
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.TEACHER

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private val helpBottomSheet = HelpBottomSheet()

    private var selectedRole = STUDENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController =
            (binding.fragmentContainerView.getFragment<NavHostFragment>()).findNavController()

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        listener()
    }

    private fun listener() {
        binding.btContinueLogin.setOnClickListener {
            // The continue button is pressed
            onContinueButtonClicked()
        }
        binding.ivHelpLogin.setOnClickListener {
            // The help button is pressed
            showHelpBottomSheet()
        }
        binding.tvAcceptanceTerms.setOnClickListener {
            // If the AcceptanceTerms text is pressed, the Terms page will open
            val intent = Intent(this, TermsActivity::class.java)
            startActivity(intent)
        }
    }

    fun changeAppColor(role: String) {
        // It changes the color of the app based on the role sent
        val color = if (role == TEACHER) R.color.teacher_color else R.color.student_color
        selectedRole = role

        binding.btContinueLogin.setBackgroundColor(
            ContextCompat.getColor(this, color)
        )

        binding.ivLogo.setColorFilter(
            ContextCompat.getColor(this, color), android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.ivHelpLogin.setColorFilter(
            ContextCompat.getColor(this, color), android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    fun playPauseLoadingAnim(isLoading: Boolean) {
        // If the information is being received from the server, an animation will be played
        if (isLoading) {
            binding.animationViewLoginReg.visibility = View.VISIBLE
            binding.btContinueLogin.text = null
            binding.animationViewLoginReg.playAnimation()
        } else {
            binding.animationViewLoginReg.visibility = View.GONE
            binding.btContinueLogin.text = "ادامه"
            binding.animationViewLoginReg.pauseAnimation()
        }
    }

    private fun onContinueButtonClicked() {
        // Check which page is being displayed
        when (navController.currentDestination?.label) {
            "fragment_login_main" -> {
                // Accessing the checkInput function inside the LoginMainFragment
                val loginMainFragment =
                    navHostFragment.childFragmentManager.fragments.firstOrNull { it is LoginMainFragment } as? LoginMainFragment
                loginMainFragment?.checkInput()
            }

            "fragment_login_otp" -> {
                // Accessing the checkInput function inside the LoginOtpFragment
                val loginOtpFragment =
                    navHostFragment.childFragmentManager.fragments.firstOrNull { it is LoginOtpFragment } as? LoginOtpFragment
                loginOtpFragment?.checkOtpCode()
            }

            "fragment_register" -> {
                // Accessing the checkInputs function inside the RegisterFragment
                val registerFragment =
                    navHostFragment.childFragmentManager.fragments.firstOrNull { it is RegisterFragment } as? RegisterFragment
                registerFragment?.checkInputs()
            }
        }
    }

    private fun showHelpBottomSheet() {
        // Checking if the Help Bottom Sheet is currently displayed or not
        if (!helpBottomSheet.isVisible) {
            // If it is not showing, it will show it
            val bundle = Bundle()

            bundle.putString(SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY, selectedRole)
            bundle.putString(
                SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY,
                when (navController.currentDestination?.label) {
                    "fragment_login_main" -> LOGIN_MAIN
                    "fragment_login_otp" -> LOGIN_OTP
                    "fragment_register" -> REGISTER_MAIN
                    "fragment_teacher_spec" -> REGISTER_TEACHER
                    else -> ""
                }
            )

            //Display Help Bottom Sheet
            helpBottomSheet.show(supportFragmentManager, null)
            helpBottomSheet.arguments = bundle
        }
    }
}