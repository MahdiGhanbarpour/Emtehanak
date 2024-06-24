package ir.mahdighanbarpour.khwarazmiapp.features.examListScreen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityExamsListBinding
import ir.mahdighanbarpour.khwarazmiapp.features.examDetailScreen.ExamDetailActivity
import ir.mahdighanbarpour.khwarazmiapp.model.data.Exam
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsResult
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_TO_EXAM_DETAIL_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_GRADE
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.hideKeyboard
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class ExamsListActivity : AppCompatActivity(), ExamListAdapter.ExamListEvents {

    private lateinit var binding: ActivityExamsListBinding
    private lateinit var examListAdapter: ExamListAdapter
    private lateinit var grade: String

    private val examListViewModel: ExamListViewModel by viewModel()
    private val sharedPreferences: SharedPreferences by inject()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        grade = sharedPreferences.getString(USER_GRADE, "")!!

        initExamListRecycler()
        initData()
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
        binding.etSearchExamList.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(binding.root)
                initData(binding.etSearchExamList.text.toString())
            }
            false
        }
    }

    private fun initExamListRecycler() {
        examListAdapter = ExamListAdapter(arrayListOf(), this)
        binding.recyclerExamList.adapter = examListAdapter

        binding.recyclerExamList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun initData(search: String = "") {
        // Checking if the user has internet or not
        if (isInternetAvailable(this)) {
            binding.ivErrorExamList.visibility = View.GONE

            playLoadingAnim()
            if (search.isNotEmpty()) searchExams(search) else getExams()
        } else {
            // Display the error to the user
            binding.ivErrorExamList.visibility = View.VISIBLE

            examListAdapter.setData(arrayListOf())

            Snackbar.make(binding.root, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE)
                .setAction("تلاش مجدد") { initData() }.show()
        }
    }

    private fun playLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(examListViewModel.isDataLoading.subscribe {
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

    private fun getExams() {
        examListViewModel.getExamList(grade).asyncRequest()
            .subscribe(object : SingleObserver<ExamsMainResult> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Snackbar.make(
                        binding.root, "خطا در دریافت اطلاعات", Snackbar.LENGTH_LONG
                    ).setAction(
                        "تلاش دوباره"
                    ) { initData() }.show()
                }

                override fun onSuccess(t: ExamsMainResult) {
                    // Checking if exams have been found for the submitted grade
                    if (t.result.exams.isEmpty()) {
                        Snackbar.make(
                            binding.root,
                            "آزمونی برای پایه تحصیلی شما یافت نشد!",
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

    private fun setExamListRecyclerData(exams: ExamsResult) {
        examListAdapter.setData(exams.exams)
    }

    private fun searchExams(search: String) {
        examListViewModel.searchExams(grade, search).asyncRequest()
            .subscribe(object : SingleObserver<ExamsMainResult> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Snackbar.make(
                        binding.root, "خطا در دریافت اطلاعات", Snackbar.LENGTH_LONG
                    ).setAction(
                        "تلاش دوباره"
                    ) { initData(search) }.show()
                }

                override fun onSuccess(t: ExamsMainResult) {
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