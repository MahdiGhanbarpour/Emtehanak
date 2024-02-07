package ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ItemAttachmentExamBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.Attachment

class ExamAttachmentAdapter(
    private var data: ArrayList<Attachment>
) : RecyclerView.Adapter<ExamAttachmentAdapter.ExamAttachmentAdapterViewHolder>() {

    private lateinit var binding: ItemAttachmentExamBinding

    inner class ExamAttachmentAdapterViewHolder(binding: ItemAttachmentExamBinding) :
        ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: Attachment) {
            binding.tvAttachmentDetailItem.text = data.detail

            // Load image
            Glide.with(binding.root.context).load(data.image).error(R.drawable.img_error)
                .centerInside().into(binding.ivAttachmentImageItem)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ExamAttachmentAdapterViewHolder {
        binding =
            ItemAttachmentExamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExamAttachmentAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ExamAttachmentAdapterViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<Attachment>) {
        this.data = data
        notifyDataSetChanged()
    }
}