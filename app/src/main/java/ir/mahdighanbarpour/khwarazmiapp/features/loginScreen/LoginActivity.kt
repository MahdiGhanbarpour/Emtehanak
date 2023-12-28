package ir.mahdighanbarpour.khwarazmiapp.features.loginScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val helpBottomSheet = HelpBottomSheet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply focus on the Text Input Layout
        binding.etLayoutNumLogin.requestFocus()

        listener()
    }

    private fun listener() {
        binding.btContinueLogin.setOnClickListener {
            checkInput()
        }
        binding.radioGroupRole.setOnCheckedChangeListener { _, id ->
            // Change the Text Input Layout hint based on the user's selected role
            binding.etLayoutNumLogin.hint = when (id) {
                R.id.radioBtStudent -> "شماره تلفن همراه دانش آموز"
                R.id.radioBtTeacher -> "شماره تلفن همراه دبیر"
                else -> "شماره تلفن همراه"
            }
        }
        binding.ivHelpLogin.setOnClickListener {
            //Display Help Bottom Sheet
            helpBottomSheet.show(supportFragmentManager, null)
        }
    }

    // Checking the validity of the entered phone number
    private fun checkInput() {
        val enteredNum = binding.etNumLogin.text.toString()

        if (enteredNum.length != 11) {
            binding.etLayoutNumLogin.error = "شماره تلفن همراه معتبر نیست"
        } else if (!enteredNum.startsWith("09")) {
            binding.etLayoutNumLogin.error = "شماره تلفن همراه می بایست با 09 اغاز شود"
        } else {
            binding.etLayoutNumLogin.error = null
        }
    }
}