package ir.mahdighanbarpour.khwarazmiapp.features.termsScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityTermsBinding

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
            finish()
        }
    }
}