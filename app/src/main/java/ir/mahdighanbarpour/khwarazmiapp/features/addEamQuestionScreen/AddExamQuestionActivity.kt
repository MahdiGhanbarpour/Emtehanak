package ir.mahdighanbarpour.khwarazmiapp.features.addEamQuestionScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityAddExamQuestionBinding
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_CREATED_EXAM_GRADE_TO_ADD_EXAM_QUESTION_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_CREATED_EXAM_ID_TO_ADD_EXAM_QUESTION_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast

class AddExamQuestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExamQuestionBinding
    private lateinit var addExamQuestionAdapter: AddExamQuestionAdapter

    private var parentExamId = -1
    private var parentExamGrade = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExamQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parentExamId = intent.getIntExtra(SEND_CREATED_EXAM_ID_TO_ADD_EXAM_QUESTION_PAGE_KEY, -1)
        parentExamGrade =
            intent.getStringExtra(SEND_CREATED_EXAM_GRADE_TO_ADD_EXAM_QUESTION_PAGE_KEY) ?: ""

        if (parentExamId == -1 || parentExamGrade == "") {
            makeShortToast(this, "خطا در ساخت آزمون")
            finish()
        }

        initAddExamQuestionRecycler(listOf())
        listener()
    }

    private fun listener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.viewAddExamQuestion.setOnClickListener {

        }
    }

    private fun initAddExamQuestionRecycler(data: List<String>) {
        addExamQuestionAdapter = AddExamQuestionAdapter(data)
        binding.recyclerAddExamQuestion.adapter = addExamQuestionAdapter
        binding.recyclerAddExamQuestion.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }
}