package ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityExamMainBinding
import ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen.adapters.ExamAttachmentAdapter
import ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen.adapters.ExamOptionAdapter
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.ExitBottomSheet
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.HelpBottomSheet
import ir.mahdighanbarpour.khwarazmiapp.model.data.Attachment
import ir.mahdighanbarpour.khwarazmiapp.model.data.Option
import ir.mahdighanbarpour.khwarazmiapp.model.data.Question
import ir.mahdighanbarpour.khwarazmiapp.utils.EXAM_MAIN
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_CORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_INCORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_QUESTION_TO_EXAM_MAIN_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_UNANSWERED_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.alphaAnimation
import ir.mahdighanbarpour.khwarazmiapp.utils.changeStatusBarColor
import ir.mahdighanbarpour.khwarazmiapp.utils.getParcelableArray
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import ir.mahdighanbarpour.khwarazmiapp.utils.translateAnimation

class ExamMainActivity : AppCompatActivity(), ExamOptionAdapter.ExamOptionEvents {

    private lateinit var binding: ActivityExamMainBinding
    private lateinit var examOptionAdapter: ExamOptionAdapter
    private lateinit var examAttachmentAdapter: ExamAttachmentAdapter
    private lateinit var questions: Array<Question>

    private var isQuestionAnswered = false
    private var questionPosition = 0
    private var correctAnswersCount = 0
    private var incorrectAnswersCount = 0
    private var unansweredCount = 0

    private val helpBottomSheet = HelpBottomSheet()
    private val exitBottomSheet = ExitBottomSheet()
    private val examResultBottomSheet = ExamResultBottomSheet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        questions = intent.getParcelableArray(SEND_SELECTED_EXAM_QUESTION_TO_EXAM_MAIN_PAGE_KEY)!!

        changeStatusBarColor(window, "#20A84D", false)
        initOptionsRecycler()
        initAttachmentRecycler()
        setQuestionData()
        listener()
    }

    private fun listener() {
        binding.ivBack.setOnClickListener {
            exitBottomSheet.show(supportFragmentManager, null)
        }
        binding.btNextExamMain.setOnClickListener {
            if (questionPosition == questions.size - 1) {
                // Checking if the Exam Result Bottom Sheet is currently displayed or not
                if (!examResultBottomSheet.isVisible) {
                    val bundle = Bundle()

                    bundle.putInt(
                        SEND_CORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY,
                        correctAnswersCount
                    )
                    bundle.putInt(
                        SEND_INCORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY,
                        incorrectAnswersCount
                    )
                    bundle.putInt(
                        SEND_UNANSWERED_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY, unansweredCount
                    )

                    examResultBottomSheet.arguments = bundle

                    //Display Exam Result Bottom Sheet
                    examResultBottomSheet.show(supportFragmentManager, null)
                }
            } else {
                questionPosition++
                binding.btNextExamMain.visibility = View.INVISIBLE

                startQuestionAnim()
            }
        }
        binding.cardViewDontKnowExamMain.setOnClickListener {
            if (!isQuestionAnswered) {

                isQuestionAnswered = true
                unansweredCount++

                examOptionAdapter.unanswered()
                binding.btNextExamMain.visibility = View.VISIBLE

                if (questionPosition == questions.size - 1) {
                    binding.btNextExamMain.text = "دیدن نتایج"
                    binding.btNextExamMain.icon = null
                    binding.btNextExamMain.setBackgroundColor(
                        ContextCompat.getColor(
                            this, R.color.teacher_color
                        )
                    )
                }
            }
        }
        binding.cardViewReportQuestionExamMain.setOnClickListener {
            makeShortToast(this, "گزارش شما ثبت شد")
        }
        binding.ivHelpExamMain.setOnClickListener {
            // The help button is pressed
            showHelpBottomSheet()
        }
    }

    private fun initOptionsRecycler() {
        val data = mutableListOf<Option>()

        examOptionAdapter = ExamOptionAdapter(data, this)
        binding.recyclerOptionsExamMain.adapter = examOptionAdapter

        binding.recyclerOptionsExamMain.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun initAttachmentRecycler() {
        val data = arrayListOf<Attachment>()

        examAttachmentAdapter = ExamAttachmentAdapter(data)
        binding.recyclerAttachmentExamMain.adapter = examAttachmentAdapter

        binding.recyclerAttachmentExamMain.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    @SuppressLint("SetTextI18n")
    private fun setQuestionData() {
        binding.tvQuestionExamMain.text = questions[questionPosition].question
        binding.tvStateExamMain.text = (questionPosition + 1).toString() + " از " + questions.size

        val animator = ValueAnimator.ofInt(
            binding.progressStateExamMain.progress, (100 / (questions.size - 1)) * questionPosition
        )
        animator.duration = 500

        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            binding.progressStateExamMain.progress = progress
        }

        animator.start()

        isQuestionAnswered = false

        val data = questions[questionPosition].options

        examOptionAdapter = ExamOptionAdapter(data.toMutableList(), this)
        binding.recyclerOptionsExamMain.adapter = examOptionAdapter

        examAttachmentAdapter.setData(questions[questionPosition].attachments)
    }

    private fun startQuestionAnim() {
        val firstAnim = AnimationSet(true)
        firstAnim.addAnimation(translateAnimation(0f, 1100f, 0f, 0f, 450))
        firstAnim.addAnimation(alphaAnimation(1f, 0f, 450))

        binding.cardViewQuestionExamMain.startAnimation(firstAnim)
        binding.recyclerOptionsExamMain.startAnimation(firstAnim)
        binding.cardViewDontKnowExamMain.startAnimation(firstAnim)

        firstAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                setQuestionData()

                val secondAnim = AnimationSet(true)
                secondAnim.addAnimation(translateAnimation(-1000f, 0f, 0f, 0f, 450))
                secondAnim.addAnimation(alphaAnimation(0f, 1f, 450))

                binding.cardViewQuestionExamMain.startAnimation(secondAnim)
                binding.recyclerOptionsExamMain.startAnimation(secondAnim)
                binding.cardViewDontKnowExamMain.startAnimation(secondAnim)
            }
        })
    }

    override fun onOptionClicked(isSelectedOptionCorrect: Boolean) {
        isQuestionAnswered = true

        binding.btNextExamMain.visibility = View.VISIBLE

        if (isSelectedOptionCorrect) {
            correctAnswersCount++
        } else {
            incorrectAnswersCount++
        }

        if (questionPosition == questions.size - 1) {
            binding.btNextExamMain.text = "دیدن نتایج"
            binding.btNextExamMain.icon = null
            binding.btNextExamMain.setBackgroundColor(
                ContextCompat.getColor(
                    this, R.color.teacher_color
                )
            )
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
        exitBottomSheet.show(supportFragmentManager, null)
    }
}