package ir.mahdighanbarpour.khwarazmiapp.features.addExamScreen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityAddExamBinding
import ir.mahdighanbarpour.khwarazmiapp.features.addExamQuestionScreen.AddExamQuestionActivity
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_CREATED_EXAM_IMAGE_TO_ADD_EXAM_QUESTION_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_CREATED_EXAM_TO_ADD_EXAM_QUESTION_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_FULL_NAME
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_PHONE_NUM
import ir.mahdighanbarpour.khwarazmiapp.utils.changeStatusBarColor
import ir.mahdighanbarpour.khwarazmiapp.utils.getFileFromUri
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import org.koin.android.ext.android.inject
import java.io.File

class AddExamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExamBinding
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var uploadImage: Uri

    private val sharedPreferences: SharedPreferences by inject()

    private val lessonItems = listOf(
        "جامع",
        "قرآن",
        "پیام های آسمان",
        "فارسی",
        "نگارش",
        "ریاضی",
        "علوم تجربی",
        "فرهنگ و هنر",
        "عربی",
        "انگلیسی",
        "کار و فناوری",
        "آمادگی دفاعی",
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

        // Changing the status bar color
        changeStatusBarColor(window, "#2d7de2", false)
        setDifficultyAdapter()
        setLessonAdapter()
        setGradeAdapter()
        pickImageSetting()
        listener()
    }

    private fun listener() {
        binding.ivBack.setOnClickListener {
            // Finish the activity
            finish()
        }
        binding.ivExamImageAddExam.setOnClickListener {
            // If the pick image button is pressed, the permission will be checked
            checkAndRequestPermission()
        }
        binding.viewPickImageAddExam.setOnClickListener {
            // If the pick image button is pressed, the permission will be checked
            checkAndRequestPermission()
        }
        binding.btAddExam.setOnClickListener {
            // If the add exam button is pressed, the data will be sent to the server
            if (binding.animationViewAddExam.visibility == View.GONE) {
                checkInputs()
            }
        }
        binding.ivDeleteExamImageAddExam.setOnClickListener {
            // If the delete button is pressed, the image will be deleted
            binding.cardViewExamImageAddExam.visibility = View.GONE

            uploadImage = Uri.EMPTY
            binding.ivExamImageAddExam.setImageDrawable(null)
        }
        binding.switchQuestionCorrectAnswerAddExam.setOnCheckedChangeListener { _, isChecked ->
            // If the switch is checked, the answer will be correct
            if (isChecked) {
                binding.switchChangeAnswerAddExam.isChecked = false
            }
        }
        binding.switchChangeAnswerAddExam.setOnCheckedChangeListener { _, isChecked ->
            // If the switch is checked, the answer will be changed
            if (isChecked) {
                binding.switchQuestionCorrectAnswerAddExam.isChecked = false
            }
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
                // Handle the result
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri: Uri? = result.data?.data
                    // Handle the selected image
                    uri?.let {
                        binding.cardViewExamImageAddExam.visibility = View.VISIBLE
                        binding.ivExamImageAddExam.setImageURI(it)
                        uploadImage = it
                    } ?: run {
                        // Handle the case where the image is not selected
                        makeShortToast(this@AddExamActivity, "تصویری انتخاب نشده است")
                    }
                }
            }

        // Request permission launcher
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

    // Request permission
    private fun checkAndRequestPermission() {
        // Check if the permission is already granted
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

    // Get intent for gallery
    private fun getIntentForGallery(): Intent {
        return Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
    }

    // Check inputs
    private fun checkInputs() {
        // Getting the data from the user
        val name = binding.etExamNameAddExam.text.toString()
        val description = binding.etExamDescAddExam.text.toString()
        val grade = binding.etGradeAddExam.text.toString()
        val lesson = binding.etLessonAddExam.text.toString()
        val price = binding.etExamPriceAddExam.text.toString()
        val difficulty = binding.etDifficultyAddExam.text.toString()
        val teacherMessage = binding.etTeacherMessageAddExam.text.toString()
        val totalPercent = binding.switchTotalPercentAddExam.isChecked
        val questionCorrectAnswer = binding.switchQuestionCorrectAnswerAddExam.isChecked
        val changeAnswer = binding.switchChangeAnswerAddExam.isChecked
        val backToPrevious = binding.switchBackToPreviousAddExam.isChecked

        val isNameOk: Boolean
        val isGradeOk: Boolean
        val isLessonOk: Boolean
        val isPriceOk: Boolean
        val isDifficultyOk: Boolean

        // Checking if the data is not empty
        if (name.isEmpty()) {
            binding.etLayoutExamNameAddExam.error = "لطفا نام آزمون را وارد کنید"
            isNameOk = false
        } else {
            binding.etLayoutExamNameAddExam.isErrorEnabled = false
            isNameOk = true
        }

        // Checking if the data is not empty
        if (grade.isEmpty()) {
            binding.etLayoutGradeAddExam.error = "لطفا پایه را وارد کنید"
            isGradeOk = false
        } else {
            // Checking if the data is in the list
            if (grade !in gradeItems) {
                binding.etLayoutGradeAddExam.error = "پایه معتبر نمی باشد"
                isGradeOk = false
            } else {
                binding.etLayoutGradeAddExam.isErrorEnabled = false
                isGradeOk = true
            }
        }

        // Checking if the data is not empty
        if (price.isEmpty()) {
            binding.etLayoutExamPriceAddExam.error = "لطفا قیمت آزمون را وارد کنید"
            isPriceOk = false
        } else {
            binding.etLayoutExamPriceAddExam.isErrorEnabled = false
            isPriceOk = true
        }

        // Checking if the data is not empty
        if (lesson.isEmpty()) {
            binding.etLayoutLessonAddExam.error = "لطفا درس را وارد کنید"
            isLessonOk = false
        } else {
            // Checking if the data is in the list
            if (lesson !in lessonItems) {
                binding.etLayoutLessonAddExam.error = "درس معتبر نمی باشد"
                isLessonOk = false
            } else {
                binding.etLayoutLessonAddExam.isErrorEnabled = false
                isLessonOk = true
            }
        }

        // Checking if the data is not empty
        if (difficulty.isEmpty()) {
            binding.etLayoutDifficultyAddExam.error = "لطفا سطح آزمون را وارد کنید"
            isDifficultyOk = false
        } else {
            // Checking if the data is in the list
            if (difficulty !in difficultyItems) {
                binding.etLayoutDifficultyAddExam.error = "سطح آزمون معتبر نمی باشد"
                isDifficultyOk = false
            } else {
                binding.etLayoutDifficultyAddExam.isErrorEnabled = false
                isDifficultyOk = true
            }
        }

        // Checking if the data is not empty
        if (isNameOk && isGradeOk && isLessonOk && isPriceOk && isDifficultyOk) {
            // Getting the user's information
            val authorName = sharedPreferences.getString(USER_FULL_NAME, null)!!
            val authorPhoneNumber = sharedPreferences.getString(USER_PHONE_NUM, null)!!

            // Convert the data to a JSON string
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
                showQuestionAnswer = questionCorrectAnswer,
                changeAnswer = changeAnswer,
                backToPrevious = backToPrevious
            )
        }
    }

    // Convert the data to a JSON string
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
        changeAnswer: Boolean,
        backToPrevious: Boolean
    ) {
        // Create an ExamRequest object
        val examData = ExamRequest(
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
            changeAnswer = changeAnswer,
            backToPrevious = backToPrevious,
            showQuestionAnswer = showQuestionAnswer,
            visibility = "1",
            questions = arrayListOf()
        )
        // Checking if the image is selected
        if (this::uploadImage.isInitialized) {
            // Convert the image to a file
            val file = getFileFromUri(this, uploadImage)
            if (file != null) {
                // Send the exam to add question screen
                showAddQuestionScreen(examData, file)
            } else {
                // Error report to user
                Snackbar.make(
                    binding.root, "خطا در پردازش تصویر", Snackbar.LENGTH_LONG
                ).setAction(
                    "تلاش دوباره"
                ) { checkInputs() }.show()
            }
        } else {
            // Send the exam to add question screen
            showAddQuestionScreen(examData, null)
        }
    }

    // Show the add question screen
    private fun showAddQuestionScreen(data: ExamRequest, image: File?) {
        val intent = Intent(this, AddExamQuestionActivity::class.java)

        // Send the exam to add question screen
        intent.putExtra(SEND_CREATED_EXAM_TO_ADD_EXAM_QUESTION_PAGE_KEY, data)
        intent.putExtra(SEND_CREATED_EXAM_IMAGE_TO_ADD_EXAM_QUESTION_PAGE_KEY, image)

        startActivity(intent)
    }
}