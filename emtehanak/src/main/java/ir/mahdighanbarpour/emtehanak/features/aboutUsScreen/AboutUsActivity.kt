package ir.mahdighanbarpour.emtehanak.features.aboutUsScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.mahdighanbarpour.emtehanak.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
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