package ir.mahdighanbarpour.khwarazmiapp.features.lessonScreen

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityLessonBinding
import ir.mahdighanbarpour.khwarazmiapp.features.examListScreen.ExamsListActivity
import ir.mahdighanbarpour.khwarazmiapp.features.lessonBookScreen.LessonBookActivity
import ir.mahdighanbarpour.khwarazmiapp.model.data.Lesson
import ir.mahdighanbarpour.khwarazmiapp.utils.MEDIA_BASE_URL
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_LESSON_PDF_TO_LESSON_BOOK_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_LESSON_TO_EXAM_LIST_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_LESSON_TO_LESSON_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.getParcelable

class LessonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLessonBinding
    private lateinit var lesson: Lesson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        listener()
    }

    private fun listener() {
        binding.ivBack.setOnClickListener {
            // If the back button is pressed, the page will be closed
            finish()
        }
        binding.linearLayoutTopStartItemLessonsMain.setOnClickListener {
            val intent = Intent(this, ExamsListActivity::class.java)
            intent.putExtra(SEND_SELECTED_LESSON_TO_EXAM_LIST_PAGE_KEY, lesson.name)
            startActivity(intent)
        }
        binding.linearLayoutTopEndItemLessonsMain.setOnClickListener {
            val intent = Intent(this, LessonBookActivity::class.java)
            intent.putExtra(SEND_SELECTED_LESSON_PDF_TO_LESSON_BOOK_PAGE_KEY, lesson.pdf)
            startActivity(intent)
        }
    }

    private fun initUi() {
        // Dragging the edge of the views to the edge of the screen based on the user's Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
        window.statusBarColor = Color.TRANSPARENT

        // Get selected lesson
        lesson = intent.getParcelable(SEND_SELECTED_LESSON_TO_LESSON_PAGE_KEY)

        // Load image
        Glide.with(binding.root.context).load(MEDIA_BASE_URL + lesson.image)
            .error(R.drawable.img_error).apply(RequestOptions().centerCrop())
            .into(binding.ivLessonImage)
    }
}