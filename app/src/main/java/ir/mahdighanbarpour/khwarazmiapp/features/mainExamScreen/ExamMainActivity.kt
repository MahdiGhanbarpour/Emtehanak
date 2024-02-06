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
import ir.mahdighanbarpour.khwarazmiapp.model.data.Question
import ir.mahdighanbarpour.khwarazmiapp.model.data.QuestionOptions
import ir.mahdighanbarpour.khwarazmiapp.utils.alphaAnimation
import ir.mahdighanbarpour.khwarazmiapp.utils.changeStatusBarColor
import ir.mahdighanbarpour.khwarazmiapp.utils.translateAnimation

class ExamMainActivity : AppCompatActivity(), ExamOptionAdapter.ExamOptionEvents {

    private lateinit var binding: ActivityExamMainBinding
    private lateinit var examOptionAdapter: ExamOptionAdapter
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
                "خروجی کد زیر چیست ؟\n" + "\n" + "spam = 7\n" + "if spam > 5:\n" + "   print(\"five\")\n" + "if spam > 8:\n" + "   print(\"eight\")",
                mutableListOf(
                    QuestionOptions(
                        "خروجی ای ندارد", false
                    ),
                    QuestionOptions(
                        "five", true
                    ),
                    QuestionOptions(
                        "eight", false
                    ),
                )
            ),
            Question(
                2,
                "خروجی کد زیر چیست ؟\n" + "\n" + "if (1 == 1) and (2 + 2 > 3):\n" + "   print(\"true\")\n" + "else:\n" + "   print(\"false\")",
                mutableListOf(
                    QuestionOptions(
                        "false", false
                    ),
                    QuestionOptions(
                        "true", true
                    ),
                )
            ),
            Question(
                3,
                "خروجی کد زیر چیست ؟\n" + "\n" + "if 1 + 1 == 2:\n" + "   if 2 * 2 == 8:\n" + "       print(\"if\")\n" + "   else:\n" + "       print(\"else\")",
                mutableListOf(
                    QuestionOptions(
                        "if", false
                    ),
                    QuestionOptions(
                        "خروجی ای ندارد", false
                    ),
                    QuestionOptions(
                        "else", true
                    ),
                    QuestionOptions(
                        "ارور", false
                    ),
                )
            ),
            Question(
                4,
                "خروجی کد زیر چیست ؟\n" + "\n" + "if 1 + 1 * 3 == 6:\n" + "   print(\"Yes\")\n" + "else:\n" + "   print(\"No\")",
                mutableListOf(
                    QuestionOptions(
                        "No", true
                    ),
                    QuestionOptions(
                        "Yes", false
                    ),
                )
            ),
            Question(
                5,
                "خروجی کد پایتون زیر چه خواهد بود؟\n\ni = 1\nwhile True:\n  if i%3 == 0:\n        break\n   print(i)\n\n    i + = 1",
                mutableListOf(
                    QuestionOptions(
                        "1 2 3", false
                    ),
                    QuestionOptions(
                        "ارور", true
                    ),
                    QuestionOptions(
                        "1 2", false
                    ),
                    QuestionOptions(
                        "هیچ یک از موارد ذکر شده", false
                    ),
                )
            ),
            Question(
                6,
                "خروجی قطعه کد پایتون زیر چه خواهد بود؟\n\nfor i in [1, 2, 3, 4][::-1]:\n    print (i)",
                mutableListOf(
                    QuestionOptions(
                        "4 3 2 1", true
                    ),
                    QuestionOptions(
                        "ارور", false
                    ),
                    QuestionOptions(
                        "1 2 3 4", false
                    ),
                    QuestionOptions(
                        "هیچ یک از موارد ذکر شده", false
                    ),
                )
            ),
            Question(
                7,
                "خروجی کد پایتون زیر چه خواهد بود؟\n\nx = 'abcd'\nfor i in range(len(x)):\n    print(i)",
                mutableListOf(
                    QuestionOptions(
                        "error", false
                    ),
                    QuestionOptions(
                        "1 2 3 4", false
                    ),
                    QuestionOptions(
                        "a b c d", false
                    ),
                    QuestionOptions(
                        "0 1 2 3", true
                    ),
                )
            ),
        )

        changeStatusBarColor(window, "#20A84D", false)
        initRecycler()
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

    private fun initRecycler() {
        val data = mutableListOf<QuestionOptions>()

        examOptionAdapter = ExamOptionAdapter(data, this)
        binding.recyclerOptionsExamMain.adapter = examOptionAdapter

        binding.recyclerOptionsExamMain.layoutManager =
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