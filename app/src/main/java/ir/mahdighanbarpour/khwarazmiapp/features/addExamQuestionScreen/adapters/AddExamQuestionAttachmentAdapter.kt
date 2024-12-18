package ir.mahdighanbarpour.khwarazmiapp.features.addExamQuestionScreen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import ir.mahdighanbarpour.khwarazmiapp.databinding.ItemAddExamQuestionAttachmentBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.AddQuestionAttachment

class AddExamQuestionAttachmentAdapter(
    private var data: MutableList<AddQuestionAttachment>,
    private var attachmentEvents: AttachmentEvents
) : RecyclerView.Adapter<AddExamQuestionAttachmentAdapter.AddExamQuestionAttachmentAdapterViewHolder>() {

    private lateinit var binding: ItemAddExamQuestionAttachmentBinding

    inner class AddExamQuestionAttachmentAdapterViewHolder(binding: ItemAddExamQuestionAttachmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: AddQuestionAttachment) {
            // Set the image and description of the attachment
            binding.ivAttachmentImageAddExamQuestion.setImageURI(data.image.toUri())
            binding.tvAttachmentDetailAddExamQuestion.text = data.description

            // Set the click listener for the delete button
            binding.ivDeleteExamImageAddExamQuestion.setOnClickListener {
                attachmentEvents.onDeleteAttachmentClick(data, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): AddExamQuestionAttachmentAdapterViewHolder {
        binding = ItemAddExamQuestionAttachmentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AddExamQuestionAttachmentAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(
        holder: AddExamQuestionAttachmentAdapterViewHolder, position: Int
    ) {
        holder.bindData(data[position])
    }

    interface AttachmentEvents {
        fun onDeleteAttachmentClick(attachment: AddQuestionAttachment, position: Int)
    }
}