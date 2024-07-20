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
import ir.mahdighanbarpour.khwarazmiapp.utils.MEDIA_BASE_URL
import ir.mahdighanbarpour.khwarazmiapp.utils.loadImageWithRetry

class ExamOptionAdapter(
    private val data: MutableList<Option>,
    private val examOptionEvents: ExamOptionEvents,
    private val showCorrectOption: Boolean,
    private val changeAnswer: Boolean
) : RecyclerView.Adapter<ExamOptionAdapter.ExamOptionAdapterViewHolder>() {

    private lateinit var binding: ItemExamOptionBinding

    private var isOptionsCorrect = MutableList<Boolean?>(data.size) { null }

    inner class ExamOptionAdapterViewHolder(binding: ItemExamOptionBinding) :
        ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: Option) {
            // Set data
            binding.tvOptionNumber.text = (adapterPosition + 1).toString()
            binding.tvOption.text = data.option

            if (showCorrectOption) {
                // Checking whether the option to put the adapter on is correct or not
                if (isOptionsCorrect[adapterPosition] == true) {
                    whenOptionIsCorrect()
                } else if (isOptionsCorrect[adapterPosition] == false) {
                    whenOptionIsIncorrect()
                }
            } else {
                if (isOptionsCorrect[adapterPosition] == true) {
                    whenOptionIsSelected()
                }
            }

            if (data.image != null) {
                // Load image
                loadImageWithRetry(
                    binding.root, binding.ivOptionImageItem, MEDIA_BASE_URL + data.image, 5
                )
            }

            // add click listener
            itemView.setOnClickListener {
                if (changeAnswer) {
                    examOptionEvents.onOptionClicked(this@ExamOptionAdapter.data[adapterPosition])
                } else {
                    if (isOptionsCorrect.indexOf(true) == -1) {
                        examOptionEvents.onOptionClicked(this@ExamOptionAdapter.data[adapterPosition])
                    }
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

    private fun whenOptionIsSelected() {
        // Change the color of the wrong option
        binding.cardViewExamOption.setCardBackgroundColor(
            ContextCompat.getColor(
                binding.root.context, R.color.orange
            )
        )
        binding.tvOption.setTextColor(
            ContextCompat.getColor(
                binding.root.context, R.color.white
            )
        )
        binding.tvOptionNumber.setTextColor(
            ContextCompat.getColor(
                binding.root.context, R.color.white
            )
        )

        binding.tvOptionNumber.setBackgroundResource(R.drawable.ic_correct)
        binding.tvOptionNumber.text = ""
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkOptions(selectedItemPosition: Int) {
        if (showCorrectOption) {
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
        } else {
            isOptionsCorrect.fill(null)
            isOptionsCorrect[selectedItemPosition] = true
        }

        notifyItemChanged(selectedItemPosition)
    }

    fun questionAnswered(option: Option) {
        checkOptions(data.indexOf(option))
    }

    interface ExamOptionEvents {
        fun onOptionClicked(option: Option)
    }
}