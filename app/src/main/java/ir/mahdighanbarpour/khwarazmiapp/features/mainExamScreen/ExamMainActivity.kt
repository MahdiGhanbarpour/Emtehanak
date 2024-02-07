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
import ir.mahdighanbarpour.khwarazmiapp.model.data.Question
import ir.mahdighanbarpour.khwarazmiapp.model.data.QuestionOptions
import ir.mahdighanbarpour.khwarazmiapp.utils.alphaAnimation
import ir.mahdighanbarpour.khwarazmiapp.utils.changeStatusBarColor
import ir.mahdighanbarpour.khwarazmiapp.utils.translateAnimation

class ExamMainActivity : AppCompatActivity(), ExamOptionAdapter.ExamOptionEvents {

    private lateinit var binding: ActivityExamMainBinding
    private lateinit var examOptionAdapter: ExamOptionAdapter
    private lateinit var examAttachmentAdapter: ExamAttachmentAdapter
    private lateinit var questions: MutableList<Question>

    private var questionPosition = 0
    private var correctAnswersCount = 0
    private var incorrectAnswersCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        questions = mutableListOf(
            Question(
                1,
                "مجموعه اعداد صحيح زوج سه رقمی كه مضرب ۳ می باشند، دارای چند عضو است؟",
                mutableListOf(
                    QuestionOptions(
                        "300", true
                    ),
                    QuestionOptions(
                        "166", false
                    ),
                    QuestionOptions(
                        "150", false
                    ),
                ),
                arrayListOf()
            ),
            Question(
                2, "روی محور زير، نقطه A نمايش دهنده چه عددی است؟", mutableListOf(
                    QuestionOptions(
                        "√5", false
                    ),
                    QuestionOptions(
                        "1-√5", false
                    ),
                    QuestionOptions(
                        "-2+√5", true
                    ),
                    QuestionOptions(
                        "2−√5", false
                    ),
                ), arrayListOf(
                    Attachment(
                        "",
                        "https://gama.ir/uploads/azmoonImages/pic_775e74864db2fa0f01646549848c9bf1.png"
                    )
                )
            ),
            Question(
                3, "قسمت رنگی شکل رو به رو، تصویر وِن کدام مجموعه است؟", mutableListOf(
                    QuestionOptions(
                        "A∩(B∪C)", false
                    ),
                    QuestionOptions(
                        "(A∩B)∪C", false
                    ),
                    QuestionOptions(
                        "A∪(B∩C)", false
                    ),
                    QuestionOptions(
                        "(A∪B)∩C", true
                    ),
                ), arrayListOf(
                    Attachment(
                        "",
                        "https://gama.ir/uploads/azmoonImages/pic_f8d7d236b8eb578d6c3e078e8a7c0140.png"
                    )
                )
            ),
            Question(
                4,
                "می دانیم چهار ضعلی ABCD مستطیل و AC و BD قطرهای آن هستند. کدام گزینه نادرست است؟",
                mutableListOf(
                    QuestionOptions(
                        "AB=DC", false
                    ),
                    QuestionOptions(
                        "BC||AD", false
                    ),
                    QuestionOptions(
                        "ABM=ACD", false
                    ),
                    QuestionOptions(
                        "BAM=DAC", true
                    ),
                ),
                arrayListOf(
                    Attachment(
                        "",
                        "https://gama.ir/uploads/azmoonImages/pic_aea48db29e35e694eab761e72bbeafdc.png"
                    )
                )
            ),
        )

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
        val data = mutableListOf<QuestionOptions>()

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

        examOptionAdapter = ExamOptionAdapter(data, this)
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