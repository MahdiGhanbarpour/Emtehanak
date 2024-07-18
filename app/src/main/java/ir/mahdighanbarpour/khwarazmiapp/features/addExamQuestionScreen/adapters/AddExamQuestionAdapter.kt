package ir.mahdighanbarpour.khwarazmiapp.features.addExamQuestionScreen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.mahdighanbarpour.khwarazmiapp.databinding.ItemAddExamQuestionBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.AddQuestion

class AddExamQuestionAdapter(
    private var data: List<AddQuestion>, private var addExamQuestionEvents: AddExamQuestionEvents
) : RecyclerView.Adapter<AddExamQuestionAdapter.AddExamQuestionAdapterViewHolder>() {

    private lateinit var binding: ItemAddExamQuestionBinding

    inner class AddExamQuestionAdapterViewHolder(binding: ItemAddExamQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: AddQuestion) {
            // Set the question and question number
            binding.tvQuestionNumberAddExamQuestion.text = "سوال ${adapterPosition + 1}"
            binding.tvQuestionAddExamQuestion.text = data.question

            // Set the click listener for the delete button
            binding.ivDeleteQuestionAddExamQuestion.setOnClickListener {
                addExamQuestionEvents.onDeleteQuestionClick(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): AddExamQuestionAdapterViewHolder {
        binding =
            ItemAddExamQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddExamQuestionAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AddExamQuestionAdapterViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    interface AddExamQuestionEvents {
        fun onDeleteQuestionClick(question: AddQuestion)
    }
}