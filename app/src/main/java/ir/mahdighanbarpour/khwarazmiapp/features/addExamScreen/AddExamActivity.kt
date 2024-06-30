package ir.mahdighanbarpour.khwarazmiapp.features.addExamScreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityAddExamBinding
import ir.mahdighanbarpour.khwarazmiapp.features.addEamQuestionScreen.AddExamQuestionActivity
import ir.mahdighanbarpour.khwarazmiapp.model.data.AddExamMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamSent
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_CREATED_EXAM_GRADE_TO_ADD_EXAM_QUESTION_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_CREATED_EXAM_ID_TO_ADD_EXAM_QUESTION_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_FULL_NAME
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_PHONE_NUM
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.changeStatusBarColor
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AddExamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExamBinding
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var uploadImage: Uri

    private val addExamViewModel: AddExamViewModel by viewModel()
    private val sharedPreferences: SharedPreferences by inject()

    private val compositeDisposable = CompositeDisposable()

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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
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
        binding.btAddExam.setOnClickListener {
            if (binding.animationViewAddExam.visibility == View.GONE) {
                checkInputs()
            }
        }
        binding.ivDeleteExamImageAddExam.setOnClickListener {
            binding.cardViewExamImageAddExam.visibility = View.GONE

            uploadImage = Uri.EMPTY
            binding.ivExamImageAddExam.setImageDrawable(null)
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
                        binding.cardViewExamImageAddExam.visibility = View.VISIBLE
                        binding.ivExamImageAddExam.setImageURI(it)
                        uploadImage = it
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
                Snackbar.make(
                    binding.root, "مجوز دسترسی به گالری داده نشده است", Snackbar.LENGTH_LONG
                ).setAction("باشه") {}.show()
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

    private fun checkInputs() {
        val name = binding.etExamNameAddExam.text.toString()
        val description = binding.etExamDescAddExam.text.toString()
        val grade = binding.etGradeAddExam.text.toString()
        val lesson = binding.etLessonAddExam.text.toString()
        val price = binding.etExamPriceAddExam.text.toString()
        val difficulty = binding.etDifficultyAddExam.text.toString()
        val teacherMessage = binding.etTeacherMessageAddExam.text.toString()
        val totalPercent = binding.switchTotalPercentAddExam.isChecked
        val questionCorrectAnswer = binding.switchQuestionCorrectAnswerAddExam.isChecked

        val isNameOk: Boolean
        val isGradeOk: Boolean
        val isLessonOk: Boolean
        val isPriceOk: Boolean
        val isDifficultyOk: Boolean


        if (name.isEmpty()) {
            binding.etLayoutExamNameAddExam.error = "لطفا نام آزمون را وارد کنید"
            isNameOk = false
        } else {
            binding.etLayoutExamNameAddExam.isErrorEnabled = false
            isNameOk = true
        }

        if (grade.isEmpty()) {
            binding.etLayoutGradeAddExam.error = "لطفا پایه را وارد کنید"
            isGradeOk = false
        } else {
            if (grade !in gradeItems) {
                binding.etLayoutGradeAddExam.error = "پایه معتبر نمی باشد"
                isGradeOk = false
            } else {
                binding.etLayoutGradeAddExam.isErrorEnabled = false
                isGradeOk = true
            }
        }

        if (price.isEmpty()) {
            binding.etLayoutExamPriceAddExam.error = "لطفا قیمت آزمون را وارد کنید"
            isPriceOk = false
        } else {
            binding.etLayoutExamPriceAddExam.isErrorEnabled = false
            isPriceOk = true
        }

        if (lesson.isEmpty()) {
            binding.etLayoutLessonAddExam.error = "لطفا درس را وارد کنید"
            isLessonOk = false
        } else {
            if (lesson !in lessonItems) {
                binding.etLayoutLessonAddExam.error = "درس معتبر نمی باشد"
                isLessonOk = false
            } else {
                binding.etLayoutLessonAddExam.isErrorEnabled = false
                isLessonOk = true
            }
        }

        if (difficulty.isEmpty()) {
            binding.etLayoutDifficultyAddExam.error = "لطفا سطح آزمون را وارد کنید"
            isDifficultyOk = false
        } else {
            if (difficulty !in difficultyItems) {
                binding.etLayoutDifficultyAddExam.error = "سطح آزمون معتبر نمی باشد"
                isDifficultyOk = false
            } else {
                binding.etLayoutDifficultyAddExam.isErrorEnabled = false
                isDifficultyOk = true
            }
        }

        if (isNameOk && isGradeOk && isLessonOk && isPriceOk && isDifficultyOk) {
            // Checking the user's internet connection
            if (isInternetAvailable(this)) {
                playLoadingAnim()

                // Getting the user's information
                val authorName = sharedPreferences.getString(USER_FULL_NAME, null)!!
                val authorPhoneNumber = sharedPreferences.getString(USER_PHONE_NUM, null)!!

                convertData(
                    name = name,
                    description = description,
                    grade = grade,
                    lesson = lesson,
                    authorName = authorName,
                    authorPhoneNumber = authorPhoneNumber,
                    price = price,
                    difficulty = difficulty,
                    teacherMessage = teacherMessage,
                    showTotalPercent = totalPercent,
                    showQuestionAnswer = questionCorrectAnswer
                )
            } else {
                // Display the error of not connecting to the Internet
                Snackbar.make(
                    binding.root, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE
                ).setAction("تلاش مجدد") { checkInputs() }.show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun playLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(addExamViewModel.isDataLoading.subscribe {
            runOnUiThread {
                if (it) {
                    binding.animationViewAddExam.visibility = View.VISIBLE
                    binding.btAddExam.text = null
                    binding.btAddExam.icon = null
                    binding.animationViewAddExam.playAnimation()
                } else {
                    binding.animationViewAddExam.visibility = View.GONE
                    binding.btAddExam.text = "مرحله بعد"
                    binding.btAddExam.icon = ContextCompat.getDrawable(this, R.drawable.ic_arrow)
                    binding.animationViewAddExam.pauseAnimation()
                }
            }
        })
    }

    private fun convertData(
        name: String,
        description: String,
        grade: String,
        lesson: String,
        authorName: String,
        authorPhoneNumber: String,
        price: String,
        difficulty: String,
        teacherMessage: String,
        showTotalPercent: Boolean,
        showQuestionAnswer: Boolean,
    ) {
        val examData = ExamSent(
            name = name,
            description = description,
            grade = grade,
            lesson = lesson,
            authorName = authorName,
            authorPhoneNumber = authorPhoneNumber,
            price = price,
            difficulty = difficulty,
            teacherMessage = teacherMessage,
            showTotalPercent = showTotalPercent,
            showQuestionAnswer = showQuestionAnswer
        )
        // Checking if the image is selected
        if (this::uploadImage.isInitialized) {
            // Convert the image to a file
            val file = getFileFromUri(uploadImage)
            if (file != null) {
                // Convert the file to a RequestBody
                val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", "exam_image.jpg", requestFile)

                // Convert the exam data to a JSON string
                val gson = Gson()
                val examDataJson = gson.toJson(examData)
                val dataPart = examDataJson.toRequestBody("multipart/form-data".toMediaTypeOrNull())

                // Add the exam to the server
                addExam(dataPart, body, grade)
            } else {
                // Error report to user
                Snackbar.make(
                    binding.root, "خطا در پردازش تصویر", Snackbar.LENGTH_LONG
                ).setAction(
                    "تلاش دوباره"
                ) { checkInputs() }.show()
            }
        } else {
            val x = ""
            val requestFile = x.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", x, requestFile)

            val gson = Gson()
            val examDataJson = gson.toJson(examData)
            val dataPart = examDataJson.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            addExam(dataPart, body, grade)
        }

    }

    private fun addExam(data: RequestBody, image: MultipartBody.Part, grade: String) {
        addExamViewModel.addExam(data, image).asyncRequest()
            .subscribe(object : SingleObserver<AddExamMainResult> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    // Error report to user
                    Snackbar.make(
                        binding.root, "خطا در دریافت اطلاعات", Snackbar.LENGTH_LONG
                    ).setAction(
                        "تلاش دوباره"
                    ) { checkInputs() }.show()
                    Log.v("testLog", e.message.toString())
                }

                override fun onSuccess(t: AddExamMainResult) {
                    Log.v("testLog", Gson().toJson(t))
                    if (t.status == 200) {
                        val intent =
                            Intent(this@AddExamActivity, AddExamQuestionActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.putExtra(
                            SEND_CREATED_EXAM_ID_TO_ADD_EXAM_QUESTION_PAGE_KEY, t.result.id
                        )
                        intent.putExtra(
                            SEND_CREATED_EXAM_GRADE_TO_ADD_EXAM_QUESTION_PAGE_KEY, grade
                        )
                        startActivity(intent)
                    } else {
                        Snackbar.make(
                            binding.root, "خطایی در افزودن آزمون رخ داد", Snackbar.LENGTH_LONG
                        ).setAction(
                            "تلاش دوباره"
                        ) { checkInputs() }.show()
                    }
                }
            })
    }

    private fun getFileFromUri(uri: Uri): File? {
        val fileName = uri.lastPathSegment?.split("/")?.lastOrNull() ?: return null
        val tempFile = File(cacheDir, fileName)
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(tempFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
        } catch (e: Exception) {
            Log.v("testLog", e.message.toString())
        }
        return tempFile
    }
}