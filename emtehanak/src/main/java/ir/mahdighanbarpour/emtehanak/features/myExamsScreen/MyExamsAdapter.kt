package ir.mahdighanbarpour.emtehanak.features.myExamsScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ir.mahdighanbarpour.emtehanak.R
import ir.mahdighanbarpour.emtehanak.databinding.ItemMyExamsBinding
import ir.mahdighanbarpour.emtehanak.model.data.Exam
import ir.mahdighanbarpour.emtehanak.utils.MEDIA_BASE_URL

class MyExamsAdapter(
    private var data: List<Exam>, private val myExamsEvents: MyExamsEvents
) : RecyclerView.Adapter<MyExamsAdapter.MyExamsAdapterViewHolder>() {

    private lateinit var binding: ItemMyExamsBinding

    inner class MyExamsAdapterViewHolder(itemView: ItemMyExamsBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        fun bindData(data: Exam) {
            // Placing the sent data in the relevant views
            binding.tvMyExamsNameItem.text = data.name
            binding.tvMyExamsLessonItem.text = data.lesson

            if (data.visibility == "0") {
                binding.ivMyExamsImageInvisibleItem.visibility = View.VISIBLE
            } else {
                binding.ivMyExamsImageInvisibleItem.visibility = View.GONE
            }

            // Load image
            Glide.with(binding.root).load(MEDIA_BASE_URL + data.image).error(R.drawable.img_error)
                .apply(RequestOptions().centerCrop()).into(binding.ivMyExamsImageItem)

            // Set click listener
            itemView.setOnClickListener {
                myExamsEvents.onExamClick(data)
            }

            // Set click listener
            binding.ivOptionsMyExams.setOnClickListener {
                showPopupMenu(it, data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyExamsAdapterViewHolder {
        binding = ItemMyExamsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyExamsAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyExamsAdapterViewHolder, position: Int) {
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
        // Clear the data
        this.data = arrayListOf()

        // Add new data
        this.data = data
        notifyDataSetChanged()
    }

    private fun showPopupMenu(view: View, exam: Exam) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.menu_my_exams_options)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_edit -> {
                    myExamsEvents.onEditExam(exam)
                    true
                }

                R.id.menu_delete -> {
                    myExamsEvents.onDeleteExam(exam)
                    true
                }

                R.id.menu_toggle_visibility -> {
                    myExamsEvents.onToggleVisibility(exam)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    interface MyExamsEvents {
        fun onExamClick(exam: Exam)
        fun onEditExam(exam: Exam)
        fun onDeleteExam(exam: Exam)
        fun onToggleVisibility(exam: Exam)
    }
}