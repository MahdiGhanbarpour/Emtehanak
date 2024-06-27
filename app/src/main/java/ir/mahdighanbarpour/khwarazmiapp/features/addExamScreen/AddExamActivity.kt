package ir.mahdighanbarpour.khwarazmiapp.features.addExamScreen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityAddExamBinding
import ir.mahdighanbarpour.khwarazmiapp.utils.changeStatusBarColor
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import java.io.ByteArrayOutputStream

class AddExamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExamBinding
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val lessonItems = listOf(
        "جامع",
        "علوم تجربی",
        "ریاضی",
        "ادبیات فارسی",
        "زبان انگلیسی",
        "دینی",
        "آمادگی دفاعی",
        "زبان عربی",
        "مطالعات اجتماعی",
        "هنر",
        "کار و فناوری"
    )
    private val gradeItems = listOf(
        "اول",
        "دوم",
        "سوم",
        "چهارم",
        "پنجم",
        "ششم",
        "هفتم",
        "هشتم",
        "نهم",
        "دهم",
        "یازدهم",
        "دوازدهم"
    )
    private val difficultyItems = listOf("آسان", "متوسط", "سخت")

    private var uploadImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor(window, "#2d7de2", false)
        setDifficultyAdapter()
        setLessonAdapter()
        setGradeAdapter()
        pickImageSetting()
        listener()
    }

    private fun listener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivExamImageAddExam.setOnClickListener {
            checkAndRequestPermission()
        }
        binding.viewPickImageAddExam.setOnClickListener {
            checkAndRequestPermission()
        }
    }

    // Setting the dropdown data of the grade
    private fun setGradeAdapter() {
        val gradeAdapter = ArrayAdapter(this, R.layout.layout_drop_down_items, gradeItems)
        binding.etGradeAddExam.setAdapter(gradeAdapter)
    }

    // Setting the dropdown data of the lesson
    private fun setLessonAdapter() {
        val lessonAdapter = ArrayAdapter(this, R.layout.layout_drop_down_items, lessonItems)
        binding.etLessonAddExam.setAdapter(lessonAdapter)
    }

    // Setting the dropdown data of the lesson
    private fun setDifficultyAdapter() {
        val difficultyAdapter = ArrayAdapter(this, R.layout.layout_drop_down_items, difficultyItems)
        binding.etDifficultyAddExam.setAdapter(difficultyAdapter)
    }

    private fun pickImageSetting() {
        // Register for Activity Results in onCreate()
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri: Uri? = result.data?.data
                    uri?.let {
                        imagePicked(it)
                    } ?: run {
                        makeShortToast(this@AddExamActivity, "تصویری انتخاب نشده است")
                    }
                }
            }

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, launch the gallery
                pickImageLauncher.launch(getIntentForGallery())
            } else {
                // Handle permission denial
                Snackbar.make(binding.root, "مجوز دسترسی به گالری داده نشده است", Snackbar.LENGTH_LONG).setAction("باشه") {}
                    .show()
            }
        }
    }

    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted. Launch the gallery.
            pickImageLauncher.launch(getIntentForGallery())
        } else {
            // Request the permission
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun getIntentForGallery(): Intent {
        return Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
    }

    private fun imagePicked(uri: Uri) {
        binding.cardViewExamImageAddExam.visibility = View.VISIBLE

        try {
            val bitmap: Bitmap

            uri.let {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap = MediaStore.Images.Media.getBitmap(
                        this.contentResolver, it
                    )
                    binding.ivExamImageAddExam.setImageBitmap(bitmap)
                } else {
                    val source = ImageDecoder.createSource(this.contentResolver, it)
                    bitmap = ImageDecoder.decodeBitmap(source)
                    binding.ivExamImageAddExam.setImageBitmap(bitmap)
                }
            }

            uploadImage = encodeImageBase64(bitmap)

        } catch (e: Exception) {
            makeShortToast(this, "خطایی در انتخاب تصویر رخ داد")
        }
    }

    private fun encodeImageBase64(bitmap: Bitmap): String {
        val byteArray = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
        val byte = byteArray.toByteArray()

        return Base64.encodeToString(byte, 0)
    }
}