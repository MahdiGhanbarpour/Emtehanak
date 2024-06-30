package ir.mahdighanbarpour.khwarazmiapp.features.addEamQuestionScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.mahdighanbarpour.khwarazmiapp.databinding.ItemAddExamQuestionBinding

class AddExamQuestionAdapter(private var data: List<String>) :
    RecyclerView.Adapter<AddExamQuestionAdapter.AddExamQuestionAdapterViewHolder>() {

    private lateinit var binding: ItemAddExamQuestionBinding

    inner class AddExamQuestionAdapterViewHolder(binding: ItemAddExamQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: String) {
            binding.tvQuestionNumberAddExamQuestion.text = "سوال $adapterPosition"
            binding.tvQuestionAddExamQuestion.text = data
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
}