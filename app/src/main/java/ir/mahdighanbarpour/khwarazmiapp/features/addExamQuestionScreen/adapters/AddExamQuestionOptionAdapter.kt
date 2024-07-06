package ir.mahdighanbarpour.khwarazmiapp.features.addExamQuestionScreen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ItemAddExamQuestionOptionBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.Option

class AddExamQuestionOptionAdapter(
    private var data: MutableList<Option>, private var addExamOptionEvents: AddExamOptionEvents
) : RecyclerView.Adapter<AddExamQuestionOptionAdapter.AddExamQuestionOptionAdapterViewHolder>() {

    private lateinit var binding: ItemAddExamQuestionOptionBinding

    inner class AddExamQuestionOptionAdapterViewHolder(binding: ItemAddExamQuestionOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: Option) {
            binding.tvOption.text = data.option
            binding.tvOptionNumber.setBackgroundResource(if (data.isCorrect) R.drawable.ic_correct else R.drawable.ic_incorrect)

            binding.ivDeleteExamOptionAddExamQuestion.setOnClickListener {
                addExamOptionEvents.onOptionClick(data, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): AddExamQuestionOptionAdapterViewHolder {
        binding = ItemAddExamQuestionOptionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AddExamQuestionOptionAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(
        holder: AddExamQuestionOptionAdapterViewHolder, position: Int
    ) {
        holder.bindData(data[position])
    }

    interface AddExamOptionEvents {
        fun onOptionClick(option: Option, position: Int)
    }
}