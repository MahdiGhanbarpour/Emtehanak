package ir.mahdighanbarpour.khwarazmiapp.features.addExamQuestionScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityAddExamQuestionBinding
import ir.mahdighanbarpour.khwarazmiapp.features.addExamQuestionScreen.adapters.AddExamQuestionAdapter
import ir.mahdighanbarpour.khwarazmiapp.features.mainTeacherScreen.TeacherMainActivity
import ir.mahdighanbarpour.khwarazmiapp.model.data.AddExamMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.AddQuestion
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_CREATED_EXAM_IMAGE_TO_ADD_EXAM_QUESTION_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_CREATED_EXAM_TO_ADD_EXAM_QUESTION_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.changeStatusBarColor
import ir.mahdighanbarpour.khwarazmiapp.utils.getParcelable
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.Calendar
import java.util.UUID

class AddExamQuestionActivity : AppCompatActivity(), AddExamQuestionAdapter.AddExamQuestionEvents {

    private lateinit var binding: ActivityAddExamQuestionBinding
    private lateinit var addExamQuestionAdapter: AddExamQuestionAdapter
    private lateinit var exam: ExamRequest

    private val addExamQuestionViewModel: AddExamQuestionViewModel by viewModel()
    private val addExamQuestionBottomSheet: AddExamQuestionBottomSheet =
        AddExamQuestionBottomSheet()
    private val compositeDisposable = CompositeDisposable()

    private var examImage: File? = null
    private var data = mutableListOf<AddQuestion>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExamQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor(window, "#2d7de2", false)
        getData()
        initAddExamQuestionRecycler(listOf())
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
        binding.viewAddExamQuestion.setOnClickListener {
            showAddExamQuestionBottomSheet()
        }
        binding.btAddExamQuestion.setOnClickListener {
            if (binding.animationViewAddExamQuestion.visibility == View.GONE) {
                checkData()
            }
        }
    }

    private fun getData() {
        exam = intent.getParcelable(SEND_CREATED_EXAM_TO_ADD_EXAM_QUESTION_PAGE_KEY)
        examImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(
                SEND_CREATED_EXAM_IMAGE_TO_ADD_EXAM_QUESTION_PAGE_KEY, File::class.java
            )
        } else {
            intent.getSerializableExtra(SEND_CREATED_EXAM_IMAGE_TO_ADD_EXAM_QUESTION_PAGE_KEY) as File?
        }

        Log.v("testLog", examImage.toString())

        if (!this::exam.isInitialized) {
            makeShortToast(this, "خطا در ساخت آزمون")
            finish()
        }
    }

    private fun initAddExamQuestionRecycler(data: List<AddQuestion>) {
        addExamQuestionAdapter = AddExamQuestionAdapter(data, this)
        binding.recyclerAddExamQuestion.adapter = addExamQuestionAdapter
        binding.recyclerAddExamQuestion.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    fun getBottomSheetData(data: AddQuestion) {
        this.data.add(data)
        initAddExamQuestionRecycler(this.data)
    }

    private fun checkData() {
        if (isInternetAvailable(this)) {
            exam.questions = data

            // Convert the exam data to a JSON string
            val gson = Gson()
            val examJson = gson.toJson(exam)

            // Convert the file to a RequestBody
            val requestFile = examImage?.asRequestBody("image/*".toMediaTypeOrNull())
                ?: "".toRequestBody("image/*".toMediaTypeOrNull())
            val examImagePart = MultipartBody.Part.createFormData(
                "image",
                "${exam.authorPhoneNumber}${UUID.randomUUID()}${Calendar.getInstance().timeInMillis}.jpg",
                requestFile
            )

            val parts = mutableListOf<MultipartBody.Part>()
            parts.add(MultipartBody.Part.createFormData("data", examJson))
            parts.add(examImagePart)

            val attachments = data.flatMap { it.attachment.map { attachment -> attachment.image } }

            attachments.forEachIndexed { index, file ->
                parts.add(
                    MultipartBody.Part.createFormData(
                        "attachment_images[$index]",
                        "Attachment${exam.authorPhoneNumber}${UUID.randomUUID()}${Calendar.getInstance().timeInMillis}.jpg",
                        file.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                )
            }

            Log.v("testLog", examJson)
            Log.v("testLog", examImage.toString())
            Log.v("testLog", parts.map { it.body }.toString())

            // Add the exam to the server
            playLoadingAnim()
            addQuestions(parts)
        } else {
            // Display the error of not connecting to the Internet
            Snackbar.make(
                binding.root, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE
            ).setAction("تلاش مجدد") { checkData() }.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun playLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(addExamQuestionViewModel.isDataLoading.subscribe {
            runOnUiThread {
                if (it) {
                    binding.animationViewAddExamQuestion.visibility = View.VISIBLE
                    binding.btAddExamQuestion.text = null
                    binding.animationViewAddExamQuestion.playAnimation()
                } else {
                    binding.animationViewAddExamQuestion.visibility = View.GONE
                    binding.btAddExamQuestion.text = "افزودن سوالات"
                    binding.animationViewAddExamQuestion.pauseAnimation()
                }
            }
        })
    }

    private fun addQuestions(parts: List<MultipartBody.Part>) {
        addExamQuestionViewModel.addExamWithQuestions(parts).asyncRequest()
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
                    ) { checkData() }.show()
                    Log.v("testLog", e.message.toString())
                }

                override fun onSuccess(t: AddExamMainResult) {
                    if (t.status == 200) {
                        val intent =
                            Intent(this@AddExamQuestionActivity, TeacherMainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        Snackbar.make(
                            binding.root, "خطایی در افزودن سوالات رخ داد", Snackbar.LENGTH_LONG
                        ).setAction(
                            "تلاش دوباره"
                        ) { checkData() }.show()
                        Log.v("testLog", t.massage)
                    }
                }
            })
    }

    private fun showAddExamQuestionBottomSheet() {
        // Checking if the Bottom Sheet is currently displayed or not
        // If it is not showing, it will show it
        if (!addExamQuestionBottomSheet.isVisible) {
            //Display Help Bottom Sheet
            addExamQuestionBottomSheet.show(supportFragmentManager, null)
        }
    }

    override fun onDeleteQuestionClick(question: AddQuestion) {
        data.remove(question)
        initAddExamQuestionRecycler(data)
    }
}