package ir.mahdighanbarpour.khwarazmiapp.features.loginScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentLoginMainBinding

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

        listener()
    }

    private fun listener() {
        binding.radioGroupRole.setOnCheckedChangeListener { _, id ->

            // Change the Text Input Layout hint based on the user's selected role
            binding.etLayoutNumLogin.hint = when (id) {
                R.id.radioBtStudent -> "شماره تلفن همراه دانش آموز"
                R.id.radioBtTeacher -> "شماره تلفن همراه دبیر"
                else -> "شماره تلفن همراه"
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
        } else {
            navController.navigate(
                R.id.action_loginMainFragment_to_loginOtpFragment
            )

            binding.etLayoutNumLogin.error = null
        }
    }
}