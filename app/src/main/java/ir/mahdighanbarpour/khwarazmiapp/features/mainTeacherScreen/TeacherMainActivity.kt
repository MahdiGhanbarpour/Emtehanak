package ir.mahdighanbarpour.khwarazmiapp.features.mainTeacherScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityTeacherMainBinding

class TeacherMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}