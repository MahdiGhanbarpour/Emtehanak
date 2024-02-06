package ir.mahdighanbarpour.khwarazmiapp.features.examDetailScreen

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityExamDetailBinding
import ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen.ExamMainActivity
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.HelpBottomSheet
import ir.mahdighanbarpour.khwarazmiapp.utils.EXAM_DETAIL
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT

class ExamDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExamDetailBinding
    private lateinit var examDetailAdapter: ExamDetailAdapter

    private val helpBottomSheet = HelpBottomSheet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
        window.statusBarColor = Color.TRANSPARENT

        initDetailRecycler()
        listener()
    }

    private fun listener() {
        binding.ivBack.setOnClickListener {
            // The continue button is pressed
            finish()
        }
        binding.ivHelpExamDetail.setOnClickListener {
            // The help button is pressed
            showHelpBottomSheet()
        }
        binding.btStartExamDetail.setOnClickListener {
            val intent = Intent(this, ExamMainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initDetailRecycler() {
        // Making the adapter and making the necessary settings
        val data = arrayListOf(
            Triple(
                "طراح", "آموزش و پرورش", R.drawable.ic_logo
            ),
            Triple(
                "وضعیت", "معتبر", R.drawable.ic_like_selected
            ),
            Triple(
                "قیمت", "رایگان", R.drawable.ic_add
            ),
            Triple(
                "پایه", "نهم", R.drawable.ic_book
            ),
            Triple(
                "درجه سختی", "متوسط", R.drawable.ic_settings
            ),
        )

        examDetailAdapter = ExamDetailAdapter(data)
        binding.recyclerDetailExamDetail.adapter = examDetailAdapter

        binding.recyclerDetailExamDetail.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    }

    private fun showHelpBottomSheet() {
        // Checking if the Help Bottom Sheet is currently displayed or not
        if (!helpBottomSheet.isVisible) {
            // If it is not showing, it will show it
            val bundle = Bundle()

            bundle.putString(SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY, STUDENT)
            bundle.putString(SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY, EXAM_DETAIL)

            //Display Help Bottom Sheet
            helpBottomSheet.show(supportFragmentManager, null)
            helpBottomSheet.arguments = bundle
        }
    }
}