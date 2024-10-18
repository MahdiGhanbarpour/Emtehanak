package ir.mahdighanbarpour.emtehanak.features.homeStudentScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ir.mahdighanbarpour.emtehanak.R
import ir.mahdighanbarpour.emtehanak.databinding.ItemCourseBinding
import ir.mahdighanbarpour.emtehanak.model.data.Lesson
import ir.mahdighanbarpour.emtehanak.utils.MEDIA_BASE_URL

class LessonsAdapter(
    private val data: List<Lesson>, private val lessonEvents: LessonEvents
) : RecyclerView.Adapter<LessonsAdapter.LessonsAdapterViewHolder>() {

    private lateinit var binding: ItemCourseBinding

    inner class LessonsAdapterViewHolder(binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: Lesson) {
            // Placing the sent data in the relevant views
            binding.tvCourseNameItem.text = data.name

            // Load image
            Glide.with(binding.root.context).load(MEDIA_BASE_URL + data.image)
                .error(R.drawable.img_error)
                .apply(RequestOptions().centerCrop()).into(binding.ivCourseImageItem)

            // One of the items has been clicked
            itemView.setOnClickListener {
                lessonEvents.onLessonClicked(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsAdapterViewHolder {
        binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LessonsAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: LessonsAdapterViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    interface LessonEvents {
        fun onLessonClicked(data: Lesson)
    }
}