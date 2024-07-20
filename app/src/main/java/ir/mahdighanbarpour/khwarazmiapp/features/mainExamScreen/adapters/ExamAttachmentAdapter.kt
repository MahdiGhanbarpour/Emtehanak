package ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ir.mahdighanbarpour.khwarazmiapp.databinding.ItemAttachmentExamBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.Attachment
import ir.mahdighanbarpour.khwarazmiapp.utils.MEDIA_BASE_URL
import ir.mahdighanbarpour.khwarazmiapp.utils.loadImageWithRetry

class ExamAttachmentAdapter(
    private var data: List<Attachment>
) : RecyclerView.Adapter<ExamAttachmentAdapter.ExamAttachmentAdapterViewHolder>() {

    private lateinit var binding: ItemAttachmentExamBinding

    inner class ExamAttachmentAdapterViewHolder(binding: ItemAttachmentExamBinding) :
        ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: Attachment) {
            // Set data
            binding.tvAttachmentDetailItem.text = data.detail

            // Load image
            loadImageWithRetry(
                binding.root, binding.ivAttachmentImageItem, MEDIA_BASE_URL + data.image, 5
            )
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
}