package ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.mahdighanbarpour.khwarazmiapp.databinding.BottomSheetExamResultBinding
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_CORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_INCORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SHOW_TOTAL_PERCENTAGE_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_TEACHER_MESSAGE_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_UNANSWERED_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
import kotlin.math.roundToInt

class ExamResultBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetExamResultBinding

    private var correctAnswersCount = 0
    private var incorrectAnswersCount = 0
    private var unansweredCount = 0
    private var teacherMessage = ""
    private var showTotalPercentage = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetExamResultBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Getting the number of correct and incorrect answers and non-answers
        correctAnswersCount = requireArguments().getInt(
            SEND_CORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
        )

        incorrectAnswersCount = requireArguments().getInt(
            SEND_INCORRECT_ANSWERS_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
        )

        unansweredCount = requireArguments().getInt(
            SEND_UNANSWERED_COUNT_TO_EXAM_RESULT_BOTTOM_SHEET_KEY
        )

        teacherMessage =
            requireArguments().getString(SEND_TEACHER_MESSAGE_TO_EXAM_RESULT_BOTTOM_SHEET_KEY, "")

        showTotalPercentage = requireArguments().getBoolean(
            SEND_SHOW_TOTAL_PERCENTAGE_TO_EXAM_RESULT_BOTTOM_SHEET_KEY, true
        )

        setMainData()
        listener()
    }

    private fun listener() {
        binding.btFinishExamResult.setOnClickListener {
            // Close this bottom sheet and close the parent activity
            dismiss()
            requireActivity().finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setMainData() {
        // Checking whether the total percentage should be displayed or not
        if (showTotalPercentage) {
            // Putting the received information in the views
            binding.tvCorrectExamResult.text = correctAnswersCount.toString()
            binding.tvIncorrectExamResult.text = incorrectAnswersCount.toString()
            binding.tvUnansweredExamResult.text = unansweredCount.toString()

            // Exam percentage calculation
            val percentage =
                (((correctAnswersCount * 3) - incorrectAnswersCount).toDouble() / ((correctAnswersCount + incorrectAnswersCount + unansweredCount).toDouble() * 3)) * 100

            // Approximation to two decimal places
            val number3digits: Double = (percentage * 1000.0).roundToInt() / 1000.0
            val number2digits: Double = (number3digits * 100.0).roundToInt() / 100.0
            val finalPercentage: Double = (number2digits * 10.0).roundToInt() / 10.0

            // Putting the received information in the views
            binding.tvTotalExamResult.text = finalPercentage.toString()
            binding.tvPercentageExamResult.text = finalPercentage.toString()
        } else {
            binding.tvPercentageExamResult.visibility = View.GONE
            binding.cardViewTotalExamResult.visibility = View.GONE
            binding.cardViewIncorrectExamResult.visibility = View.GONE

            binding.tvTitlePercentageExamResult.text = "موفق باشید"
            binding.tvTitleCorrectExamResult.text = "پاسخ داده شده"

            binding.tvCorrectExamResult.text =
                (correctAnswersCount + incorrectAnswersCount).toString()
            binding.tvUnansweredExamResult.text = unansweredCount.toString()
        }

        binding.tvTeacherDescriptionExamResult.text = teacherMessage
    }
}