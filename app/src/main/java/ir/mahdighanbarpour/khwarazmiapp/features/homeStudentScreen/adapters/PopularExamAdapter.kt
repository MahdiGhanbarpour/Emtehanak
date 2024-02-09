package ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ItemPopularExamBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.Exam

class PopularExamAdapter(
    private var data: ArrayList<Exam>, private val popularExamEvents: PopularExamEvents
) : RecyclerView.Adapter<PopularExamAdapter.PopularExamAdapterViewHolder>() {

    private lateinit var binding: ItemPopularExamBinding

    inner class PopularExamAdapterViewHolder(binding: ItemPopularExamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: Exam) {
            // Placing the sent data in the relevant views
            binding.tvPopularExamNameItem.text = data.name

            // Load image
            Glide.with(binding.root.context).load(data.image).error(R.drawable.img_error)
                .apply(RequestOptions().centerCrop()).into(binding.ivPopularExamImageItem)

            // One of the items has been clicked
            itemView.setOnClickListener {
                popularExamEvents.onPopularExamClicked(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PopularExamAdapterViewHolder {
        binding = ItemPopularExamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularExamAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PopularExamAdapterViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<Exam>) {
        this.data = data
        notifyDataSetChanged()
    }

    interface PopularExamEvents {
        fun onPopularExamClicked(data: Exam)
    }
}