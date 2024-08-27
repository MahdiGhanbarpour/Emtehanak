package ir.mahdighanbarpour.khwarazmiapp.features.profileStudentScreen

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ItemHonorBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.Honor

class StudentProfileHonorsAdapter(private val data: List<Honor>) :
    RecyclerView.Adapter<StudentProfileHonorsAdapter.StudentProfileHonorsAdapterViewHolder>() {

    private lateinit var binding: ItemHonorBinding

    inner class StudentProfileHonorsAdapterViewHolder(binding: ItemHonorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: Honor) {
            binding.tvHonorName.text = data.name
            binding.tvHonorDescription.text = data.description

            if (data.isEarned) {
                binding.ivHonorEarned.visibility = View.VISIBLE
                binding.root.strokeColor =
                    ContextCompat.getColor(binding.root.context, R.color.orange)
                binding.root.strokeWidth = 2
            } else {
                binding.ivHonorEarned.visibility = View.GONE
                binding.root.setCardBackgroundColor(Color.parseColor("#0D000000"))
            }

            Glide.with(binding.root).load(data.image).error(R.drawable.img_error)
                .apply(RequestOptions().centerCrop()).into(binding.ivHonorImage)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): StudentProfileHonorsAdapterViewHolder {
        binding = ItemHonorBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StudentProfileHonorsAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(
        holder: StudentProfileHonorsAdapterViewHolder, position: Int
    ) {
        holder.bindData(data[position])
    }
}