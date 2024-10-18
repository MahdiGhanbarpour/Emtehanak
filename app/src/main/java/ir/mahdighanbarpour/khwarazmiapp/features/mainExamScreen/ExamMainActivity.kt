package ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityExamMainBinding
import ir.mahdighanbarpour.khwarazmiapp.databinding.DialogUnansweredBinding
import ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen.adapters.ExamAttachmentAdapter
import ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen.adapters.ExamOptionAdapter
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.ExitBottomSheet
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.HelpBottomSheet
import ir.mahdighanbarpour.khwarazmiapp.model.data.Attachment
import ir.mahdighanbarpour.khwarazmiapp.model.data.Exam
import ir.mahdighanbarpour.khwarazmiapp.model.data.Option
import ir.mahdighanbarpour.khwarazmiapp.model.data.Question
import ir.mahdighanbarpour.khwarazmiapp.model.data.ReportQuestionMainResult
import ir.mahdighanbarpour.khwarazmiapp.utils.EXAM_MAIN
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_CORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_INCORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_QUESTION_TO_EXAM_MAIN_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_TO_EXAM_MAIN_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SHOW_TOTAL_PERCENTAGE_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_TEACHER_MESSAGE_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_UNANSWERED_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_PHONE_NUM
import ir.mahdighanbarpour.khwarazmiapp.utils.alphaAnimation
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.changeStatusBarColor
import ir.mahdighanbarpour.khwarazmiapp.utils.getParcelable
import ir.mahdighanbarpour.khwarazmiapp.utils.getParcelableArray
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import ir.mahdighanbarpour.khwarazmiapp.utils.onBackButtonPressed
import ir.mahdighanbarpour.khwarazmiapp.utils.translateAnimation
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Timer
import kotlin.concurrent.timerTask

class ExamMainActivity : AppCompatActivity(), ExamOptionAdapter.ExamOptionEvents {

    private lateinit var binding: ActivityExamMainBinding
    private lateinit var examOptionAdapter: ExamOptionAdapter
    private lateinit var examAttachmentAdapter: ExamAttachmentAdapter
    private lateinit var exam: Exam
    private lateinit var questions: Array<Question>
    private lateinit var userPhoneNum: String

    private var isQuestionAnswered = false
    private var questionPosition = 0
    private var correctAnswersCount = 0
    private var incorrectAnswersCount = 0

    private val examMainViewModel: ExamMainViewModel by viewModel()
    private val sharedPreferences: SharedPreferences by inject()

    private val helpBottomSheet = HelpBottomSheet()
    private val exitBottomSheet = ExitBottomSheet()
    private val examResultBottomSheet = ExamResultBottomSheet()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the list of sent questions
        questions = intent.getParcelableArray(SEND_SELECTED_EXAM_QUESTION_TO_EXAM_MAIN_PAGE_KEY)!!

        // Get the Exam Data
        exam = intent.getParcelable(SEND_SELECTED_EXAM_TO_EXAM_MAIN_PAGE_KEY)

        // Get the user phone number
        userPhoneNum = sharedPreferences.getString(USER_PHONE_NUM, "")!!

        onBackButtonPressed {
            customOnBackPressed()
        }

        // Changing the color of the Status Bar
        changeStatusBarColor(window, "#20A84D", false)

        initOptionsRecycler()
        initAttachmentRecycler(arrayListOf())
        setQuestionData()
        listener()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Clear the values in Composite Disposable
        compositeDisposable.clear()
    }


    @SuppressLint("SetTextI18n")
    private fun listener() {
        binding.ivBack.setOnClickListener {
            // Show exit bottom sheet
            exitBottomSheet.show(supportFragmentManager, null)
        }
        binding.btNextExamMain.setOnClickListener {
            // Checking question position
            if (!exam.backToPrevious && !isQuestionAnswered) {
                showUnansweredDialog()
            } else {
                goNextQuestion()
            }
        }
        binding.btPreviousExamMain.setOnClickListener {
            // Checking question position
            if (questionPosition != 0) {
                // Go to the previous question
                questionPosition--

                if (questionPosition == 0 && exam.backToPrevious) {
                    binding.btPreviousExamMain.visibility = View.INVISIBLE
                }

                startQuestionAnim(true)
            }
        }
        binding.cardViewReportQuestionExamMain.setOnClickListener {
            // Report that the question is broken
            checkInternetReportQuestion(questions[questionPosition].id)
        }
        binding.ivHelpExamMain.setOnClickListener {
            // The help button is pressed
            showHelpBottomSheet()
        }
    }

    private fun initOptionsRecycler() {
        // init Options Recycler
        val data = mutableListOf<Option>()

        examOptionAdapter =
            ExamOptionAdapter(data, this, exam.showQuestionAnswer, exam.changeAnswer)
        binding.recyclerOptionsExamMain.adapter = examOptionAdapter

        binding.recyclerOptionsExamMain.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun initAttachmentRecycler(data: List<Attachment>) {
        // init Attachment Recycler
        examAttachmentAdapter = ExamAttachmentAdapter(data)
        binding.recyclerAttachmentExamMain.adapter = examAttachmentAdapter

        binding.recyclerAttachmentExamMain.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    @SuppressLint("SetTextI18n")
    private fun setQuestionData() {
        // Putting question information in views
        binding.tvQuestionExamMain.text = questions[questionPosition].question
        binding.tvStateExamMain.text = (questionPosition + 1).toString() + " از " + questions.size

        // Changing the State Exam Main progress value
        val animator = ValueAnimator.ofInt(
            binding.progressStateExamMain.progress,
            (100 / (if (questions.size == 1) 1 else questions.size - 1)) * questionPosition
        )
        animator.duration = 500

        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            binding.progressStateExamMain.progress = progress
        }

        animator.start()

        isQuestionAnswered = false

        val data = questions[questionPosition].options

        examOptionAdapter = ExamOptionAdapter(
            data.toMutableList(), this, exam.showQuestionAnswer, exam.changeAnswer
        )
        binding.recyclerOptionsExamMain.adapter = examOptionAdapter

        initAttachmentRecycler(questions[questionPosition].attachments)

        // Checking whether the question has been answered or not
        val userSelectedOption =
            questions[questionPosition].options.indexOfFirst { it.isUserSelected }
        if (userSelectedOption != -1) {
            isQuestionAnswered = !exam.changeAnswer
            examOptionAdapter.questionAnswered(questions[questionPosition].options[userSelectedOption])
        }

        // Checking whether the question is the last one or not
        if (questionPosition == questions.size - 1) {
            // If it is, it will change the button text
            binding.btNextExamMain.text = "اتمام آزمون"
            binding.btNextExamMain.iconPadding = 0
            binding.btNextExamMain.icon = null
            binding.btNextExamMain.setBackgroundColor(
                ContextCompat.getColor(
                    this, R.color.teacher_color
                )
            )
        } else {
            binding.btNextExamMain.text = ""
            binding.btNextExamMain.iconPadding = 2
            binding.btNextExamMain.icon = ContextCompat.getDrawable(this, R.drawable.ic_arrow)
            binding.btNextExamMain.setBackgroundColor(
                ContextCompat.getColor(
                    this, R.color.student_color
                )
            )
        }
    }

    private fun startQuestionAnim(isNext: Boolean = false) {
        // Show question change animation
        val firstAnim = AnimationSet(true)
        firstAnim.addAnimation(
            translateAnimation(
                0f, if (isNext) -1100f else 1100f, 0f, 0f, 450
            )
        )
        firstAnim.addAnimation(alphaAnimation(1f, 0f, 450))

        // Start animation
        binding.cardViewQuestionExamMain.startAnimation(firstAnim)
        binding.recyclerOptionsExamMain.startAnimation(firstAnim)

        // Set animation listener
        firstAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                // Set question data
                setQuestionData()

                // Hide question change animation
                val secondAnim = AnimationSet(true)
                secondAnim.addAnimation(
                    translateAnimation(
                        if (isNext) 1000f else -1000f, 0f, 0f, 0f, 450
                    )
                )
                secondAnim.addAnimation(alphaAnimation(0f, 1f, 450))

                // Start animation
                binding.cardViewQuestionExamMain.startAnimation(secondAnim)
                binding.recyclerOptionsExamMain.startAnimation(secondAnim)

                binding.root.smoothScrollTo(0, 0)
            }
        })
    }

    private fun goNextQuestion() {
        // Checking whether the questions are finished or not
        if (questionPosition == questions.size - 1) {
            // Checking if the Exam Result Bottom Sheet is currently displayed or not
            if (!examResultBottomSheet.isVisible) {
                val bundle = Bundle()

                // calculate correct and incorrect answers count
                correctAnswersCount =
                    questions.count { it.options.indexOfFirst { option -> option.isUserSelected && option.isCorrect } != -1 }
                incorrectAnswersCount =
                    questions.count { it.options.indexOfFirst { option -> option.isUserSelected && !option.isCorrect } != -1 }

                // put data to bundle
                bundle.putInt(
                    SEND_CORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY, correctAnswersCount
                )
                bundle.putInt(
                    SEND_INCORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY,
                    incorrectAnswersCount
                )
                bundle.putInt(
                    SEND_UNANSWERED_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY,
                    questions.size - correctAnswersCount - incorrectAnswersCount
                )
                bundle.putString(
                    SEND_TEACHER_MESSAGE_TO_EXAM_RESULT_BOTTOM_SHEET_KEY, exam.teacherMessage
                )
                bundle.putBoolean(
                    SEND_SHOW_TOTAL_PERCENTAGE_TO_EXAM_RESULT_BOTTOM_SHEET_KEY,
                    exam.showTotalPercent
                )

                examResultBottomSheet.arguments = bundle

                // Display Exam Result Bottom Sheet
                examResultBottomSheet.show(supportFragmentManager, null)

                // Set a dismiss listener to close the activity when the bottom sheet is dismissed
                examResultBottomSheet.dialog?.setOnDismissListener {
                    // Close the activity
                    finish()
                }
            }
        } else {
            // Go to the next question
            questionPosition++

            if (questionPosition != 0 && exam.backToPrevious) {
                binding.btPreviousExamMain.visibility = View.VISIBLE
            }

            startQuestionAnim()
        }
    }

    private fun showUnansweredDialog() {
        // Displaying the unanswered dialog
        val dialog = AlertDialog.Builder(this).create()

        // Setting the dialog view
        val dialogBinding = DialogUnansweredBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.btNoCancel.setOnClickListener {
            // If the cancel button is pressed, the dialog will be closed
            dialog.dismiss()
        }
        dialogBinding.btYesNextQuestion.setOnClickListener {
            dialog.dismiss()
            goNextQuestion()
        }
    }

    private fun checkInternetReportQuestion(questionId: Int) {
        // Checking the user's internet connection
        if (isInternetAvailable(this)) {
            reportQuestion(questionId)
        } else {
            // Display the error of not connecting to the Internet
            Snackbar.make(
                binding.root, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE
            ).setAction("تلاش مجدد") { checkInternetReportQuestion(questionId) }.show()
        }
    }

    private fun reportQuestion(questionId: Int, retries: Int = 5) {
        // Report that the question is broken
        examMainViewModel.reportQuestion(questionId, userPhoneNum).asyncRequest()
            .subscribe(object : SingleObserver<ReportQuestionMainResult> {
                override fun onSubscribe(d: Disposable) {
                    // Add the disposable to Composite Disposable
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    if (retries > 0) {
                        // Retry
                        reportQuestion(questionId, retries - 1)
                    } else {
                        // Error report to user
                        makeShortToast(
                            this@ExamMainActivity, "خطا در گزارش سوال. لطفا دوباره تلاش کنید."
                        )
                    }
                }

                override fun onSuccess(t: ReportQuestionMainResult) {
                    // Checking result
                    if (t.status == 200) {
                        // Report success
                        makeShortToast(this@ExamMainActivity, "گزارش شما ثبت شد")
                    } else if (retries > 0) {
                        // Retry
                        reportQuestion(questionId, retries - 1)
                    } else {
                        // Error report to user
                        makeShortToast(
                            this@ExamMainActivity, "خطا در ثبت گزارش. لطفا دوباره تلاش کنید."
                        )
                    }
                }
            })
    }

    // Click on one of the options
    @SuppressLint("SetTextI18n")
    override fun onOptionClicked(option: Option) {
        // Checking whether the question has been answered or not
        isQuestionAnswered = !exam.changeAnswer

        // Make delay
        Timer().schedule(timerTask {
            runOnUiThread {
                val data = questions[questionPosition].options

                examOptionAdapter = ExamOptionAdapter(
                    data.toMutableList(),
                    this@ExamMainActivity,
                    exam.showQuestionAnswer,
                    exam.changeAnswer
                )
                binding.recyclerOptionsExamMain.adapter = examOptionAdapter

                examOptionAdapter.questionAnswered(option)
            }
        }, 100)


        if (questions[questionPosition].options.indexOfFirst { it.isUserSelected } == -1 || exam.changeAnswer) {
            questions[questionPosition].options.forEach {
                it.isUserSelected = false
            }
            questions[questionPosition].options[questions[questionPosition].options.indexOf(option)].isUserSelected =
                true
        }
    }

    private fun showHelpBottomSheet() {
        // Checking if the Help Bottom Sheet is currently displayed or not
        if (!helpBottomSheet.isVisible) {
            // If it is not showing, it will show it
            val bundle = Bundle()

            bundle.putString(SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY, STUDENT)
            bundle.putString(SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY, EXAM_MAIN)

            //Display Help Bottom Sheet
            helpBottomSheet.show(supportFragmentManager, null)
            helpBottomSheet.arguments = bundle
        }
    }

    private fun customOnBackPressed(): Boolean {
        // Show exit bottom sheet
        exitBottomSheet.show(supportFragmentManager, null)
        return true
    }
}