package ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ItemExamOptionBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.Option

class ExamOptionAdapter(
    private val data: MutableList<Option>, private val examOptionEvents: ExamOptionEvents
) : RecyclerView.Adapter<ExamOptionAdapter.ExamOptionAdapterViewHolder>() {

    private lateinit var binding: ItemExamOptionBinding

    private val isOptionsCorrect = MutableList<Boolean?>(data.size) { null }

    inner class ExamOptionAdapterViewHolder(binding: ItemExamOptionBinding) :
        ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: Option) {
            binding.tvOptionNumber.text = (adapterPosition + 1).toString()
            binding.tvOption.text = data.option

            // Checking whether the option to put the adapter on is correct or not
            if (isOptionsCorrect[adapterPosition] == true) {
                whenOptionIsCorrect()
            } else if (isOptionsCorrect[adapterPosition] == false) {
                whenOptionIsIncorrect()
            }

            itemView.setOnClickListener {
                if (isOptionsCorrect.indexOf(true) == -1) {
                    checkOptions(adapterPosition)
                    examOptionEvents.onOptionClicked(this@ExamOptionAdapter.data[adapterPosition].isCorrect)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ExamOptionAdapterViewHolder {
        binding = ItemExamOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExamOptionAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ExamOptionAdapterViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    private fun whenOptionIsCorrect() {
        // Change the color of the correct option
        binding.cardViewExamOption.setCardBackgroundColor(
            ContextCompat.getColor(
                binding.root.context, R.color.green
            )
        )
        binding.tvOption.setTextColor(
            ContextCompat.getColor(
                binding.root.context, R.color.white
            )
        )
        binding.tvOptionNumber.setBackgroundResource(R.drawable.ic_correct)
        binding.tvOptionNumber.text = ""
    }

    private fun whenOptionIsIncorrect() {
        // Change the color of the wrong option
        binding.cardViewExamOption.setCardBackgroundColor(
            ContextCompat.getColor(
                binding.root.context, R.color.red
            )
        )
        binding.tvOption.setTextColor(
            ContextCompat.getColor(
                binding.root.context, R.color.white
            )
        )
        binding.tvOptionNumber.setBackgroundResource(R.drawable.ic_incorrect)
        binding.tvOptionNumber.text = ""
    }

    private fun checkOptions(selectedItemPosition: Int) {
        // Checking whether the option selected by the user is correct or not
        if (data[selectedItemPosition].isCorrect) {
            isOptionsCorrect[selectedItemPosition] = true
        } else {
            val correctOptionIndex = data.mapIndexedNotNull { index, questionOptions ->
                index.takeIf { questionOptions.isCorrect }
            }[0]

            isOptionsCorrect[selectedItemPosition] = false
            isOptionsCorrect[correctOptionIndex] = true

            notifyItemChanged(correctOptionIndex)
        }

        notifyItemChanged(selectedItemPosition)
    }

    fun unanswered() {
        // Show the correct option
        val correctOptionIndex = data.mapIndexedNotNull { index, questionOptions ->
            index.takeIf { questionOptions.isCorrect }
        }[0]

        isOptionsCorrect[correctOptionIndex] = true
        notifyItemChanged(correctOptionIndex)
    }

    interface ExamOptionEvents {
        fun onOptionClicked(isSelectedOptionCorrect: Boolean)
    }
}