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
import ir.mahdighanbarpour.khwarazmiapp.model.data.Attachment
import ir.mahdighanbarpour.khwarazmiapp.model.data.Option
import ir.mahdighanbarpour.khwarazmiapp.model.data.Question
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_QUESTION_TO_EXAM_MAIN_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.alphaAnimation
import ir.mahdighanbarpour.khwarazmiapp.utils.changeStatusBarColor
import ir.mahdighanbarpour.khwarazmiapp.utils.getParcelableArray
import ir.mahdighanbarpour.khwarazmiapp.utils.translateAnimation

class ExamMainActivity : AppCompatActivity(), ExamOptionAdapter.ExamOptionEvents {

    private lateinit var binding: ActivityExamMainBinding
    private lateinit var examOptionAdapter: ExamOptionAdapter
    private lateinit var examAttachmentAdapter: ExamAttachmentAdapter
    private lateinit var questions: Array<Question>

    private var questionPosition = 0
    private var correctAnswersCount = 0
    private var incorrectAnswersCount = 0

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
            finish()
        }
        binding.btNextExamMain.setOnClickListener {
            if (questionPosition == questions.size - 1) {
                finish()
            } else {
                questionPosition++
                binding.btNextExamMain.visibility = View.INVISIBLE

                startQuestionAnim()
            }
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
            }
        })
    }

    override fun onOptionClicked(isSelectedOptionCorrect: Boolean?) {
        binding.btNextExamMain.visibility = View.VISIBLE

        if (isSelectedOptionCorrect != null) {
            if (isSelectedOptionCorrect) {
                correctAnswersCount++
            } else {
                incorrectAnswersCount++
            }
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
}