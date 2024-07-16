package ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import ir.mahdighanbarpour.khwarazmiapp.utils.alphaAnimation
import ir.mahdighanbarpour.khwarazmiapp.utils.changeStatusBarColor
import ir.mahdighanbarpour.khwarazmiapp.utils.getParcelable
import ir.mahdighanbarpour.khwarazmiapp.utils.getParcelableArray
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import ir.mahdighanbarpour.khwarazmiapp.utils.translateAnimation
import java.util.Timer
import kotlin.concurrent.timerTask

class ExamMainActivity : AppCompatActivity(), ExamOptionAdapter.ExamOptionEvents {

    private lateinit var binding: ActivityExamMainBinding
    private lateinit var examOptionAdapter: ExamOptionAdapter
    private lateinit var examAttachmentAdapter: ExamAttachmentAdapter
    private lateinit var exam: Exam
    private lateinit var questions: Array<Question>

    private var isQuestionAnswered = false
    private var questionPosition = 0
    private var correctAnswersCount = 0
    private var incorrectAnswersCount = 0

    private val helpBottomSheet = HelpBottomSheet()
    private val exitBottomSheet = ExitBottomSheet()
    private val examResultBottomSheet = ExamResultBottomSheet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the list of sent questions
        questions = intent.getParcelableArray(SEND_SELECTED_EXAM_QUESTION_TO_EXAM_MAIN_PAGE_KEY)!!

        // Get the Exam Data
        exam = intent.getParcelable(SEND_SELECTED_EXAM_TO_EXAM_MAIN_PAGE_KEY)

        // Changing the color of the Status Bar
        changeStatusBarColor(window, "#20A84D", false)

        Log.v("testLog", exam.changeAnswer.toString())

        initOptionsRecycler()
        initAttachmentRecycler(arrayListOf())
        setQuestionData()
        listener()
    }

    @SuppressLint("SetTextI18n")
    private fun listener() {
        binding.ivBack.setOnClickListener {
            // Show exit bottom sheet
            exitBottomSheet.show(supportFragmentManager, null)
        }
        binding.btNextExamMain.setOnClickListener {
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
//                binding.btNextExamMain.visibility = View.INVISIBLE

                if (questionPosition == 0 && exam.backToPrevious) {
                    binding.btPreviousExamMain.visibility = View.INVISIBLE
                }

                startQuestionAnim(true)
            }
        }
        binding.cardViewReportQuestionExamMain.setOnClickListener {
            // Report that the question is broken
            // TODO
            makeShortToast(this, "گزارش شما ثبت شد")
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

        if (questionPosition == questions.size - 1) {
            binding.btNextExamMain.text = "دیدن نتایج"
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

        binding.cardViewQuestionExamMain.startAnimation(firstAnim)
        binding.recyclerOptionsExamMain.startAnimation(firstAnim)

        firstAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                setQuestionData()

                val secondAnim = AnimationSet(true)
                secondAnim.addAnimation(
                    translateAnimation(
                        if (isNext) 1000f else -1000f, 0f, 0f, 0f, 450
                    )
                )
                secondAnim.addAnimation(alphaAnimation(0f, 1f, 450))

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

                correctAnswersCount =
                    questions.count { it.options.indexOfFirst { option -> option.isUserSelected && option.isCorrect } != -1 }
                incorrectAnswersCount =
                    questions.count { it.options.indexOfFirst { option -> option.isUserSelected && !option.isCorrect } != -1 }

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

                //Display Exam Result Bottom Sheet
                examResultBottomSheet.show(supportFragmentManager, null)
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

    // Click on one of the options
    @SuppressLint("SetTextI18n")
    override fun onOptionClicked(option: Option) {
        // Checking whether the question has been answered or not
        isQuestionAnswered = !exam.changeAnswer

//        binding.btNextExamMain.visibility = View.VISIBLE

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
        }, 200)


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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Show exit bottom sheet
        exitBottomSheet.show(supportFragmentManager, null)
    }
}