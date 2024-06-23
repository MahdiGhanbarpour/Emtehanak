package ir.mahdighanbarpour.khwarazmiapp.features.examListScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ItemExamListBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.Exam

class ExamListAdapter(
    private var data: ArrayList<Exam>, private val examListEvents: ExamListEvents
) : RecyclerView.Adapter<ExamListAdapter.ExamListAdapterViewHolder>() {

    private lateinit var binding: ItemExamListBinding

    inner class ExamListAdapterViewHolder(itemView: ItemExamListBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        fun bindData(data: Exam) {
            binding.tvExamListNameItem.text = data.name
            binding.tvExamListAuthorItem.text = data.authorName

            binding.tvExamListPriceItem.text =
                if (data.price == 0) "رایگان" else "${data.price} تومان"

            Glide.with(binding.root).load(data.image).error(R.drawable.img_error)
                .apply(RequestOptions().centerCrop()).into(binding.ivExamListImageItem)

            itemView.setOnClickListener {
                examListEvents.onExamClick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamListAdapterViewHolder {
        binding = ItemExamListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExamListAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ExamListAdapterViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<Exam>) {
        this.data = data
        notifyDataSetChanged()
    }

    interface ExamListEvents {
        fun onExamClick(exam: Exam)
    }
}