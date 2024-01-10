package ir.mahdighanbarpour.khwarazmiapp.features.loginScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityLoginBinding
import ir.mahdighanbarpour.khwarazmiapp.features.regScreen.RegisterFragment
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var navController: NavController

    private val helpBottomSheet = HelpBottomSheet()

    var selectedRole = STUDENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController =
            (binding.fragmentContainerView.getFragment<NavHostFragment>()).findNavController()

        listener()
    }

    private fun listener() {
        binding.btContinueLogin.setOnClickListener {

            // Check which page is being displayed
            when (navController.currentDestination?.label) {
                "fragment_login_main" -> {

                    // Accessing the checkInput function inside the LoginMainFragment
                    val navHostFragment =
                        supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                    val loginMainFragment =
                        navHostFragment.childFragmentManager.fragments.firstOrNull { it is LoginMainFragment } as? LoginMainFragment
                    loginMainFragment?.checkInput()

                }

                "fragment_login_otp" -> {

                    // Accessing the checkInput function inside the LoginOtpFragment
                    val navHostFragment =
                        supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                    val loginOtpFragment =
                        navHostFragment.childFragmentManager.fragments.firstOrNull { it is LoginOtpFragment } as? LoginOtpFragment
                    loginOtpFragment?.checkOtpCode()

                }

                "fragment_register" -> {

                    // Accessing the checkInputs function inside the RegisterFragment
                    val navHostFragment =
                        supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                    val registerFragment =
                        navHostFragment.childFragmentManager.fragments.firstOrNull { it is RegisterFragment } as? RegisterFragment
                    registerFragment?.checkInputs()
                }
            }
        }
        binding.ivHelpLogin.setOnClickListener {
            if (!helpBottomSheet.isVisible) {
                val bundle = Bundle()
                bundle.putString(SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY, selectedRole)

                //Display Help Bottom Sheet
                helpBottomSheet.show(supportFragmentManager, null)
                helpBottomSheet.arguments = bundle
            }
        }
    }

    fun changeAppColor(color: Int) {
        binding.btContinueLogin.setBackgroundColor(
            ContextCompat.getColor(this, color)
        )

        binding.ivLogo.setColorFilter(
            ContextCompat.getColor(this, color), android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.ivHelpLogin.setColorFilter(
            ContextCompat.getColor(this, color), android.graphics.PorterDuff.Mode.SRC_IN
        )

        binding.tvAcceptanceTerms.setTextColor(ContextCompat.getColor(this, color))
    }
}