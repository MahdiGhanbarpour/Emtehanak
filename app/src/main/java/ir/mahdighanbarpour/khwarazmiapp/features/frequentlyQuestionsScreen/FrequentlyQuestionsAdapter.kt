package ir.mahdighanbarpour.khwarazmiapp.features.frequentlyQuestionsScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.mahdighanbarpour.khwarazmiapp.databinding.ItemFrequentlyQuestionsBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestionsArray

class FrequentlyQuestionsAdapter(
    private val data: List<FrequentlyQuestionsArray>
) : RecyclerView.Adapter<FrequentlyQuestionsAdapter.FrequentlyQuestionsAdapterViewHolder>() {

    private lateinit var binding: ItemFrequentlyQuestionsBinding

    inner class FrequentlyQuestionsAdapterViewHolder(binding: ItemFrequentlyQuestionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: FrequentlyQuestionsArray) {
            // Placing the sent data in the relevant views
            binding.tvFrequentlyQuestions.text = data.question
            binding.tvFrequentlyQuestionsAnswer.text = data.answer
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FrequentlyQuestionsAdapterViewHolder {
        binding = ItemFrequentlyQuestionsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FrequentlyQuestionsAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FrequentlyQuestionsAdapterViewHolder, position: Int) {
        holder.bindData(data[position])
    }
}