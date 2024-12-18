package ir.mahdighanbarpour.emtehanak.features.frequentlyQuestionsScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.mahdighanbarpour.emtehanak.databinding.ItemFrequentlyQuestionsBinding
import ir.mahdighanbarpour.emtehanak.model.data.FrequentlyQuestion

class FrequentlyQuestionsAdapter(
    private val data: List<FrequentlyQuestion>
) : RecyclerView.Adapter<FrequentlyQuestionsAdapter.FrequentlyQuestionsAdapterViewHolder>() {

    private lateinit var binding: ItemFrequentlyQuestionsBinding

    inner class FrequentlyQuestionsAdapterViewHolder(binding: ItemFrequentlyQuestionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: FrequentlyQuestion) {
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