package ir.mahdighanbarpour.emtehanak.features.termsScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.mahdighanbarpour.emtehanak.databinding.ActivityTermsBinding

class TermsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener()
    }

    private fun listener() {
        binding.ivBack.setOnClickListener {
            // If the back button is pressed, the page will be closed
            finish()
        }
    }
}