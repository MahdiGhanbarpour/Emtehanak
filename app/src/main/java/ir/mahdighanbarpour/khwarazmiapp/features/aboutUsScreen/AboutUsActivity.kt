package ir.mahdighanbarpour.khwarazmiapp.features.aboutUsScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityAboutUsBinding

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
            finish()
        }
    }
}