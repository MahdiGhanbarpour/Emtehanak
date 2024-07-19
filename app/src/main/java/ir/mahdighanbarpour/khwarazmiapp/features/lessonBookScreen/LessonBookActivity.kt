package ir.mahdighanbarpour.khwarazmiapp.features.lessonBookScreen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityLessonBookBinding
import ir.mahdighanbarpour.khwarazmiapp.utils.MEDIA_BASE_URL
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_LESSON_PDF_TO_LESSON_BOOK_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LessonBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLessonBookBinding
    private lateinit var retrievePDFFromURL: RetrievePDFFromURL
    private lateinit var pdfUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the pdf url
        pdfUrl =
            MEDIA_BASE_URL + intent.getStringExtra(SEND_SELECTED_LESSON_PDF_TO_LESSON_BOOK_PAGE_KEY)!!

        checkInternet()
        listener()
    }

    private fun listener() {
        binding.fabButtonBackLessonBook.setOnClickListener {
            // If the back button is pressed, the page will be closed
            finish()
        }
        binding.fabButtonDownLessonBook.setOnClickListener {
            // If the down button is pressed, the page will be scrolled down
            binding.pdfViewLessonBook.jumpTo(binding.pdfViewLessonBook.currentPage + 1, true)
        }
        binding.fabButtonUpLessonBook.setOnClickListener {
            // If the up button is pressed, the page will be scrolled up
            binding.pdfViewLessonBook.jumpTo(binding.pdfViewLessonBook.currentPage - 1, true)
        }
    }

    private fun checkInternet() {
        // Checking the user's internet connection
        if (isInternetAvailable(this)) {
            binding.animationViewLessonBook.visibility = View.VISIBLE
            viewPDF()
        } else {
            // Display the error of not connecting to the Internet
            binding.animationViewLessonBook.visibility = View.GONE

            Snackbar.make(binding.root, "عدم اتصال به اینترنت", Snackbar.LENGTH_INDEFINITE)
                .setAction("تلاش مجدد") { checkInternet() }.show()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun viewPDF() {
        // View pdf
        retrievePDFFromURL = RetrievePDFFromURL(binding.pdfViewLessonBook, this)
        GlobalScope.launch(Dispatchers.Main) {
            retrievePDFFromURL.loadPDFFromUrl(pdfUrl)
        }
    }

    fun bookLoaded() {
        // Hide the loading animation
        binding.animationViewLessonBook.visibility = View.GONE

        // Show the buttons
        binding.fabButtonDownLessonBook.visibility = View.VISIBLE
        binding.fabButtonUpLessonBook.visibility = View.VISIBLE
    }

    fun loadError() {
        // Hide the loading animation
        runOnUiThread {
            binding.animationViewLessonBook.visibility = View.GONE
        }

        // Show the error message
        Snackbar.make(binding.root, "خطا در نمایش کتاب", Snackbar.LENGTH_INDEFINITE)
            .setAction("تلاش مجدد") { checkInternet() }.show()
    }
}