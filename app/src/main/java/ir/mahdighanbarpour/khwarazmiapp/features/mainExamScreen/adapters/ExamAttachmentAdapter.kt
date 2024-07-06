package ir.mahdighanbarpour.khwarazmiapp.features.mainExamScreen.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ItemAttachmentExamBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.Attachment
import ir.mahdighanbarpour.khwarazmiapp.utils.MEDIA_BASE_URL

class ExamAttachmentAdapter(
    private var data: List<Attachment>
) : RecyclerView.Adapter<ExamAttachmentAdapter.ExamAttachmentAdapterViewHolder>() {

    private lateinit var binding: ItemAttachmentExamBinding

    inner class ExamAttachmentAdapterViewHolder(binding: ItemAttachmentExamBinding) :
        ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(data: Attachment) {
            binding.tvAttachmentDetailItem.text = data.detail

            // Load image
            loadImageWithRetry(binding.ivAttachmentImageItem, MEDIA_BASE_URL + data.image, 5)
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

    fun loadImageWithRetry(imageView: ImageView, url: String, maxRetries: Int) {
        var retryCount = 0
        val handler = Handler(Looper.getMainLooper())

        fun loadImage() {
            val options = RequestOptions().centerInside().placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)

            Glide.with(imageView.context).load(url).apply(options)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (retryCount < maxRetries) {
                            retryCount++
                            handler.postDelayed({ loadImage() }, 1000)  // Retry after 1 second
                        } else {
                            Snackbar.make(
                                binding.root,
                                "خطا در دریافت تصویر ضمیمه سوال",
                                Snackbar.LENGTH_INDEFINITE
                            ).setAction("تلاش مجدد") {
                                loadImageWithRetry(imageView, url, maxRetries)
                            }.show()
                        }
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                }).into(imageView)
        }

        loadImage()
    }
}