package ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ItemCourseBinding

class CoursesAdapter(
    private val data: ArrayList<Pair<String, String>>, private val courseEvents: CourseEvents
) : RecyclerView.Adapter<CoursesAdapter.CoursesAdapterViewHolder>() {

    private lateinit var binding: ItemCourseBinding

    inner class CoursesAdapterViewHolder(binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: Pair<String, String>) {
            binding.tvCourseNameItem.text = data.first

            Glide.with(binding.root.context).load(data.second).error(R.drawable.img_error)
                .apply(RequestOptions().centerCrop()).into(binding.ivCourseImageItem)

            itemView.setOnClickListener {
                courseEvents.onCourseClicked(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesAdapterViewHolder {
        binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoursesAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CoursesAdapterViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    interface CourseEvents {
        fun onCourseClicked(data: Pair<String, String>)
    }
}