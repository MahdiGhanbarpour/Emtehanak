package ir.mahdighanbarpour.emtehanak.features.examsListScreen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.emtehanak.R
import ir.mahdighanbarpour.emtehanak.databinding.ActivityExamsListBinding
import ir.mahdighanbarpour.emtehanak.features.examDetailScreen.ExamDetailActivity
import ir.mahdighanbarpour.emtehanak.model.data.Exam
import ir.mahdighanbarpour.emtehanak.model.data.ExamResult
import ir.mahdighanbarpour.emtehanak.model.data.ExamsMainResult
import ir.mahdighanbarpour.emtehanak.utils.SEND_SELECTED_EXAM_TO_EXAM_DETAIL_PAGE_KEY
import ir.mahdighanbarpour.emtehanak.utils.SEND_SELECTED_LESSON_TO_EXAM_LIST_PAGE_KEY
import ir.mahdighanbarpour.emtehanak.utils.STUDENT
import ir.mahdighanbarpour.emtehanak.utils.TEACHER
import ir.mahdighanbarpour.emtehanak.utils.USER_GRADE
import ir.mahdighanbarpour.emtehanak.utils.USER_ROLE
import ir.mahdighanbarpour.emtehanak.utils.asyncRequest
import ir.mahdighanbarpour.emtehanak.utils.changeLayersColor
import ir.mahdighanbarpour.emtehanak.utils.hideKeyboard
import ir.mahdighanbarpour.emtehanak.utils.isInternetAvailable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class ExamsListActivity : AppCompatActivity(), ExamsListAdapter.ExamListEvents {

    private lateinit var binding: ActivityExamsListBinding
    private lateinit var examsListAdapter: ExamsListAdapter
    private lateinit var userGrade: String
    private lateinit var userRole: String

    private val examsListViewModel: ExamsListViewModel by viewModel()
    private val sharedPreferences: SharedPreferences by inject()

    private val compositeDisposable = CompositeDisposable()

    private var lesson: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting the user's grade and role
        userGrade = sharedPreferences.getString(USER_GRADE, "")!!
        userRole = sharedPreferences.getString(USER_ROLE, "")!!

        lesson = intent.getStringExtra(SEND_SELECTED_LESSON_TO_EXAM_LIST_PAGE_KEY)

        // Setting the background color of the buttons
        if (userRole == TEACHER) {
            binding.btFilterExamList.setBackgroundColor(
                ContextCompat.getColor(
                    this, R.color.teacher_color
                )
            )
            binding.animationViewExamList.changeLayersColor(R.color.teacher_color)
            binding.ivErrorExamList.setColorFilter(
                ContextCompat.getColor(
                    this, R.color.teacher_color
                )
            )
        }

        initExamListRecycler()
        initData()
        listener()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Clear the values in Composite Disposable
        compositeDisposable.clear()
    }


    private fun listener() {
        binding.ivBack.setOnClickListener {
            // If the back button is pressed, the page will be closed
            finish()
        }
        binding.etSearchExamList.setOnEditorActionListener { _, actionId, _ ->
            // If the search button is pressed, the search will be performed
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(binding.root)
                initData(binding.etSearchExamList.text.toString())
            }
            false
        }
    }

    // initialize recycler
    private fun initExamListRecycler() {
        examsListAdapter = ExamsListAdapter(arrayListOf(), this)
        binding.recyclerExamList.adapter = examsListAdapter

        binding.recyclerExamList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    // get data
    private fun initData(search: String = "") {
        // Checking if the user has internet or not
        if (isInternetAvailable(this)) {
            binding.ivErrorExamList.visibility = View.GONE

            playLoadingAnim()
            // If search is empty, get exams, otherwise search
            if (search.isNotEmpty()) searchExams(search) else getExams()
        } else {
            // Display the error to the user
            binding.ivErrorExamList.visibility = View.VISIBLE

            examsListAdapter.setData(arrayListOf())

            // Display the error of not connecting to the Internet
            Snackbar.make(binding.root, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE)
                .setAction("تلاش مجدد") { initData() }.show()
        }
    }

    private fun playLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(examsListViewModel.isDataLoading.subscribe {
            runOnUiThread {
                if (it) {
                    binding.recyclerExamList.visibility = View.GONE

                    binding.animationViewExamList.visibility = View.VISIBLE
                    binding.animationViewExamList.playAnimation()

                    binding.etSearchExamList.isClickable = false
                } else {
                    binding.recyclerExamList.visibility = View.VISIBLE

                    binding.animationViewExamList.visibility = View.GONE
                    binding.animationViewExamList.pauseAnimation()

                    binding.etSearchExamList.isClickable = true
                }
            }
        })
    }

    // get exams
    private fun getExams(retries: Int = 5) {
        val grade: String
        val gradeList: String

        // Checking if the user is a student or a teacher
        if (userRole == STUDENT) {
            grade = userGrade
            gradeList = ""
        } else {
            grade = ""
            gradeList = userGrade
        }

        // Getting the list of exams with the help of grade and limit
        examsListViewModel.getExamList(grade, gradeList, lesson, "-1").asyncRequest()
            .subscribe(object : SingleObserver<ExamsMainResult> {
                override fun onSubscribe(d: Disposable) {
                    // Add the disposable to Composite Disposable
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    if (retries > 0) {
                        // Retry
                        playLoadingAnim()
                        getExams(retries - 1)
                    } else {
                        // Error report to user
                        Snackbar.make(
                            binding.root, "خطا در دریافت اطلاعات", Snackbar.LENGTH_LONG
                        ).setAction(
                            "تلاش دوباره"
                        ) { initData() }.show()
                    }
                }

                override fun onSuccess(t: ExamsMainResult) {
                    // Checking if exams have been found for the submitted grade
                    if (t.result.exams.isEmpty()) {
                        Snackbar.make(
                            binding.root,
                            "آزمونی برای این پایه تحصیلی و یا این درس شما یافت نشد!",
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction(
                            "باشه"
                        ) { finish() }.show()
                    } else {
                        // Starting RecyclerView with sent data
                        setExamListRecyclerData(t.result)
                    }
                }
            })
    }

    // set data
    private fun setExamListRecyclerData(exams: ExamResult) {
        examsListAdapter.setData(exams.exams)
    }

    // search exams
    private fun searchExams(search: String, retries: Int = 5) {
        val grade: String
        val gradeList: String

        // Checking if the user is a student or a teacher
        if (userRole == STUDENT) {
            grade = userGrade
            gradeList = ""
        } else {
            grade = ""
            gradeList = userGrade
        }
        Log.v("testLog", lesson.toString())

        // Getting the list of exams with the help of grade and limit
        examsListViewModel.searchExams(grade, search, lesson, gradeList).asyncRequest()
            .subscribe(object : SingleObserver<ExamsMainResult> {
                override fun onSubscribe(d: Disposable) {
                    // Add the disposable to Composite Disposable
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    if (retries > 0) {
                        // Retry
                        playLoadingAnim()
                        searchExams(search, retries - 1)
                    } else {
                        // Error report to user
                        Snackbar.make(
                            binding.root, "خطا در دریافت اطلاعات", Snackbar.LENGTH_LONG
                        ).setAction(
                            "تلاش دوباره"
                        ) { initData(search) }.show()
                    }
                }

                override fun onSuccess(t: ExamsMainResult) {
                    // Checking if exams have been found for the submitted grade
                    setExamListRecyclerData(t.result)
                }
            })
    }

    override fun onExamClick(exam: Exam) {
        // One of the exams has been clicked
        val intent = Intent(this, ExamDetailActivity::class.java)
        intent.putExtra(SEND_SELECTED_EXAM_TO_EXAM_DETAIL_PAGE_KEY, exam)
        startActivity(intent)
    }
}