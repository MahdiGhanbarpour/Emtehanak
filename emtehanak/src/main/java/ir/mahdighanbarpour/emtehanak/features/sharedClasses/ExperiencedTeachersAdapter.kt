package ir.mahdighanbarpour.emtehanak.features.sharedClasses

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ir.mahdighanbarpour.emtehanak.R
import ir.mahdighanbarpour.emtehanak.databinding.ItemExperiencedTeachersBinding

class ExperiencedTeachersAdapter(
    private val data: ArrayList<Triple<String, String, String?>>,
    private val experiencedTeachersEvents: ExperiencedTeachersEvents
) : RecyclerView.Adapter<ExperiencedTeachersAdapter.ExperiencedTeachersAdapterViewHolder>() {

    private lateinit var binding: ItemExperiencedTeachersBinding

    inner class ExperiencedTeachersAdapterViewHolder(binding: ItemExperiencedTeachersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: Triple<String, String, String?>) {
            // Placing the sent data in the relevant views
            binding.tvExperiencedTeacherNameItem.text = data.first
            binding.tvExperiencedTeacherCourseItem.text = data.second

            binding.cardViewExperiencedTeachersProfileImageItem.setCardBackgroundColor(
                ContextCompat.getColor(binding.root.context, R.color.teacher_color)
            )

            // If the teacher has a picture, it will show the picture
            if (data.third != null) {
                binding.ivExperiencedTeachersProfileImageItem.visibility = View.VISIBLE
                binding.ivExperiencedTeachersDefaultProfileImageItem.visibility = View.GONE
                binding.cardViewExperiencedTeachersProfileImageItem.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.white)
                )

                // Load image
                Glide.with(binding.root.context).load(data.third).error(R.drawable.img_error)
                    .apply(RequestOptions().centerCrop())
                    .into(binding.ivExperiencedTeachersProfileImageItem)
            }

            // One of the items has been clicked
            itemView.setOnClickListener {
                experiencedTeachersEvents.onExperiencedTeachersClicked(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ExperiencedTeachersAdapterViewHolder {
        binding = ItemExperiencedTeachersBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ExperiencedTeachersAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ExperiencedTeachersAdapterViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    interface ExperiencedTeachersEvents {
        fun onExperiencedTeachersClicked(data: Triple<String, String, String?>)
    }
}