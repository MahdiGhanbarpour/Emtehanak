package ir.mahdighanbarpour.khwarazmiapp.features.examDetailScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityExamDetailBinding
import ir.mahdighanbarpour.khwarazmiapp.databinding.DialogExamDetailBinding
import ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen.ExamMainActivity
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.HelpBottomSheet
import ir.mahdighanbarpour.khwarazmiapp.model.data.Exam
import ir.mahdighanbarpour.khwarazmiapp.model.data.QuestionMainResult
import ir.mahdighanbarpour.khwarazmiapp.utils.EXAM_DETAIL
import ir.mahdighanbarpour.khwarazmiapp.utils.MEDIA_BASE_URL
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_QUESTION_TO_EXAM_MAIN_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_TO_EXAM_DETAIL_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_TO_EXAM_MAIN_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.getParcelable
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExamDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExamDetailBinding
    private lateinit var examDetailAdapter: ExamDetailAdapter
    private lateinit var data: Exam

    private val examViewModel: ExamViewModel by viewModel()

    private val helpBottomSheet = HelpBottomSheet()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        setMainData()
        initDetailRecycler()
        listener()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Clear the values in Composite Disposable
        compositeDisposable.clear()
    }


    private fun listener() {
        binding.ivBack.setOnClickListener {
            // If the back button is pressed, the page will be closed
            finish()
        }
        binding.ivHelpExamDetail.setOnClickListener {
            // The help button is pressed
            showHelpBottomSheet()
        }
        binding.btStartExamDetail.setOnClickListener {
            // The start exam button is pressed
            showDialog()
        }
        binding.ivLikeExamDetail.setOnClickListener {
            // Checking if this exam has already been liked or not
            if (binding.ivLikeExamDetail.isChecked) {
                // If liked, the like will be removed
                binding.ivLikeExamDetail.isChecked = false
            } else {
                // Otherwise, it will be liked and the dislike will be removed
                binding.ivLikeExamDetail.playAnimation()
                binding.ivLikeExamDetail.isChecked = true
                binding.ivDislikeExamDetail.isChecked = false
            }
        }
        binding.ivDislikeExamDetail.setOnClickListener {
            // Checking if this exam has already been disliked or not
            if (binding.ivDislikeExamDetail.isChecked) {
                // If disliked, the dislike will be removed
                binding.ivDislikeExamDetail.isChecked = false
            } else {
                // Otherwise, it will be disliked and the like will be removed
                binding.ivDislikeExamDetail.playAnimation()
                binding.ivDislikeExamDetail.isChecked = true
                binding.ivLikeExamDetail.isChecked = false
            }
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

        // Get selected exam information
        data = intent.getParcelable(SEND_SELECTED_EXAM_TO_EXAM_DETAIL_PAGE_KEY)
    }

    @SuppressLint("SetTextI18n")
    private fun playLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(examViewModel.isDataLoading.subscribe {
            runOnUiThread {
                if (it) {
                    binding.animationViewStartExamDetail.visibility = View.VISIBLE
                    binding.btStartExamDetail.text = null
                    binding.animationViewStartExamDetail.playAnimation()
                } else {
                    binding.animationViewStartExamDetail.visibility = View.GONE
                    binding.btStartExamDetail.text = "شروع آزمون"
                    binding.animationViewStartExamDetail.pauseAnimation()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setMainData() {
        // Placing received data in views
        binding.tvNameExamDetail.text = data.name
        binding.tvDescriptionExamDetail.text = data.description

        // If the exam is not free, its price will be displayed
        if (data.price != 0) {
            binding.btStartExamDetail.text = "خرید | ${data.price} تومان"
        }

        // Load image
        Glide.with(binding.root.context).load(MEDIA_BASE_URL + data.image)
            .error(R.drawable.img_error).apply(RequestOptions().centerCrop())
            .into(binding.ivImageExamDetail)
    }

    private fun initDetailRecycler() {
        // Making the adapter and making the necessary settings
        val data = arrayListOf(
            Triple(
                "طراح", this.data.authorName, R.drawable.ic_logo
            ),
            Triple(
                "قیمت",
                (if (this.data.price == 0) "رایگان" else "${this.data.price} تومان"),
                R.drawable.ic_price
            ),
            Triple(
                "پایه", this.data.grade, R.drawable.ic_book
            ),
            Triple(
                "درجه سختی", this.data.difficulty, R.drawable.ic_difficulty
            ),
        )

        if (this.data.isVerified) {
            data.addAll(
                1, arrayListOf(
                    Triple(
                        "وضعیت", "معتبر", R.drawable.ic_verify
                    )
                )
            )
        }

        examDetailAdapter = ExamDetailAdapter(data)
        binding.recyclerDetailExamDetail.adapter = examDetailAdapter

        binding.recyclerDetailExamDetail.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    }

    private fun onStartClicked() {
        // Check if the exam is free or not
        if (data.price == 0) {
            // Checking the user's internet connection
            if (isInternetAvailable(this)) {
                playLoadingAnim()
                getExamsQuestion()
            } else {
                // Display the error of not connecting to the Internet
                Snackbar.make(
                    binding.root, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE
                ).setAction("تلاش مجدد") { onStartClicked() }.show()
            }
        } else {
            // TODO
            makeShortToast(this, "با تشکر از همراهی شما! بخش پرداخت بزودی فعال خواهد شد.")
        }
    }

    private fun getExamsQuestion() {
        // Getting selected exam questions from the server with the help of exam id
        examViewModel.getExamsQuestion(data.id).asyncRequest()
            .subscribe(object : SingleObserver<QuestionMainResult> {
                override fun onSubscribe(d: Disposable) {
                    // Add the disposable to Composite Disposable
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    // Error report to user
                    Snackbar.make(
                        binding.root, "خطا در دریافت اطلاعات", Snackbar.LENGTH_LONG
                    ).setAction(
                        "تلاش دوباره"
                    ) { onStartClicked() }.show()
                    Log.v("testLog", e.message.toString())
                }

                override fun onSuccess(t: QuestionMainResult) {
                    // Check if questions have been found for this exam or not
                    if (t.result.questions.isNotEmpty()) {
                        // Send the received questions to the main page of the exam and open that page
                        val intent = Intent(this@ExamDetailActivity, ExamMainActivity::class.java)

                        intent.putExtra(
                            SEND_SELECTED_EXAM_QUESTION_TO_EXAM_MAIN_PAGE_KEY,
                            t.result.questions.toTypedArray()
                        )
                        intent.putExtra(SEND_SELECTED_EXAM_TO_EXAM_MAIN_PAGE_KEY, data)

                        startActivity(intent)
                    } else {
                        makeShortToast(this@ExamDetailActivity, "سوالی برای این آزمون یافت نشد!")
                    }
                }
            })
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

    private fun showDialog() {
        var text = ""

        text += if (data.showQuestionAnswer) {
            "✔️ پاسخ هر سوال بعد از پاسخ گویی به آن، به شما نمایش داده می شود."
        } else {
            "❌ پاسخ هر سوال بعد از پاسخ گویی به آن، به شما نمایش داده نمی شود."
        }

        text += if (data.changeAnswer) {
            "\n✔️ شما می توانید پاسخی که داده اید را تغییر بدهید."
        } else {
            "\n❌ شما نمی توانید پاسخی که داده اید را تغییر بدهید."
        }

        text += if (data.backToPrevious) {
            "\n✔️ شما می توانید به سوال قبل برگردید."
        } else {
            "\n❌ شما نمی توانید به سوال قبل برگردید."
        }

        text += if (data.showTotalPercent) {
            "\n✔️ درصد کل و جزئیات پاسخ های داده شده در آخر به شما نمایش داده می شود."
        } else {
            "\n❌ درصد کل و جزئیات پاسخ های داده شده به شما نمایش داده نمی شود."
        }

        // Displaying the dialog
        val dialog = AlertDialog.Builder(this).create()

        // Setting the dialog view
        val dialogBinding = DialogExamDetailBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialogBinding.tvExamDetail.text = text
        dialog.show()

        dialogBinding.btSureExamDetail.setOnClickListener {
            // If the cancel button is pressed, the dialog will be closed
            dialog.dismiss()
            onStartClicked()
        }
    }
}