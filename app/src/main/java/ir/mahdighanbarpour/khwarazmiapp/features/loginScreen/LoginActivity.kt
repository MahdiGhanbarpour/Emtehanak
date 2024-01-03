package ir.mahdighanbarpour.khwarazmiapp.features.loginScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var navController: NavController

    private val helpBottomSheet = HelpBottomSheet()

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
            if (navController.currentDestination?.label == "fragment_login_main") {

                // Accessing the checkInput function inside the LoginMainFragment
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                val loginMainFragment =
                    navHostFragment.childFragmentManager.fragments.firstOrNull { it is LoginMainFragment } as? LoginMainFragment
                loginMainFragment?.checkInput()
            } else if (navController.currentDestination?.label == "fragment_login_otp") {

                // Accessing the checkInput function inside the LoginOtpFragment
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                val loginOtpFragment =
                    navHostFragment.childFragmentManager.fragments.firstOrNull { it is LoginOtpFragment } as? LoginOtpFragment
                loginOtpFragment?.checkOtpCode()
            }
        }
        binding.ivHelpLogin.setOnClickListener {
            if (!helpBottomSheet.isVisible) {
                //Display Help Bottom Sheet
                helpBottomSheet.show(supportFragmentManager, null)
            }
        }
    }
}