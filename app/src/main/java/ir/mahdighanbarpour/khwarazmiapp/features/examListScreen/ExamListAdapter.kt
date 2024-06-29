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
import ir.mahdighanbarpour.khwarazmiapp.utils.MEDIA_BASE_URL

class ExamListAdapter(
    private var data: List<Exam>, private val examListEvents: ExamListEvents
) : RecyclerView.Adapter<ExamListAdapter.ExamListAdapterViewHolder>() {

    private lateinit var binding: ItemExamListBinding

    inner class ExamListAdapterViewHolder(itemView: ItemExamListBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        fun bindData(data: Exam) {
            binding.tvExamListNameItem.text = data.name
            binding.tvExamListAuthorItem.text = data.authorName

            binding.tvExamListPriceItem.text =
                if (data.price == 0) "رایگان" else "${data.price} تومان"

            Glide.with(binding.root).load(MEDIA_BASE_URL + data.image).error(R.drawable.img_error)
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Exam>) {
        this.data = arrayListOf()
        notifyDataSetChanged()

        this.data = data
        notifyDataSetChanged()
    }

    interface ExamListEvents {
        fun onExamClick(exam: Exam)
    }
}