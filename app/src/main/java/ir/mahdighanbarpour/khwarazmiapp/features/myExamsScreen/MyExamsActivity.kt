package ir.mahdighanbarpour.khwarazmiapp.features.myExamsScreen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityMyExamsBinding
import ir.mahdighanbarpour.khwarazmiapp.databinding.DialogDeleteExamBinding
import ir.mahdighanbarpour.khwarazmiapp.features.examDetailScreen.ExamDetailActivity
import ir.mahdighanbarpour.khwarazmiapp.model.data.Exam
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_TO_EXAM_DETAIL_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_TO_TOGGLE_VISIBILITY_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_PHONE_NUM
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.hideKeyboard
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyExamsActivity : AppCompatActivity(), MyExamsAdapter.MyExamsEvents {

    private lateinit var binding: ActivityMyExamsBinding
    private lateinit var myExamsAdapter: MyExamsAdapter

    private val myExamsViewModel: MyExamsViewModel by viewModel()
    private val sharedPreferences: SharedPreferences by inject()

    private val compositeDisposable = CompositeDisposable()
    private val toggleVisibilityBottomSheet = ToggleVisibilityBottomSheet()

    private var teacherPhoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyExamsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherPhoneNumber = sharedPreferences.getString(USER_PHONE_NUM, null)!!

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
        binding.etSearchMyExams.setOnEditorActionListener { _, actionId, _ ->
            // If the search button is pressed, the search will be performed
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(binding.root)
                initData(binding.etSearchMyExams.text.toString())
            }
            false
        }
    }

    // initialize recycler
    private fun initExamListRecycler() {
        myExamsAdapter = MyExamsAdapter(arrayListOf(), this)
        binding.recyclerMyExams.adapter = myExamsAdapter

        binding.recyclerMyExams.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    // get data
    private fun initData(search: String = "") {
        // Checking if the user has internet or not
        if (isInternetAvailable(this)) {
            binding.ivErrorMyExams.visibility = View.GONE

            playLoadingAnim()
            getTeachersExams(search)
        } else {
            // Display the error to the user
            binding.ivErrorMyExams.visibility = View.VISIBLE

            myExamsAdapter.setData(arrayListOf())

            // Display the error of not connecting to the Internet
            Snackbar.make(binding.root, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE)
                .setAction("تلاش مجدد") { initData() }.show()
        }
    }

    private fun playLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(myExamsViewModel.isDataLoading.subscribe {
            runOnUiThread {
                if (it) {
                    binding.recyclerMyExams.visibility = View.GONE
                    binding.animationViewMyExams.visibility = View.VISIBLE
                    binding.animationViewMyExams.playAnimation()

                    binding.etSearchMyExams.isClickable = false
                } else {
                    binding.recyclerMyExams.visibility = View.VISIBLE
                    binding.animationViewMyExams.visibility = View.GONE
                    binding.animationViewMyExams.pauseAnimation()

                    binding.etSearchMyExams.isClickable = true
                }
            }
        })
    }

    // get exams
    private fun getTeachersExams(search: String, retries: Int = 5) {
        // Getting the list of exams with the help of teacher phone num
        myExamsViewModel.getTeachersExams(teacherPhoneNumber, search).asyncRequest()
            .subscribe(object : SingleObserver<ExamsMainResult> {
                override fun onSubscribe(d: Disposable) {
                    // Add the disposable to Composite Disposable
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    if (retries > 0) {
                        // Retry
                        getTeachersExams(search, retries - 1)
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
                    // Checking if exams have been found
                    myExamsAdapter.setData(arrayListOf())

                    if (t.result.exams.isEmpty()) {
                        Snackbar.make(
                            binding.root, "آزمونی یافت نشد!", Snackbar.LENGTH_INDEFINITE
                        ).setAction(
                            "باشه"
                        ) { }.show()
                    } else {
                        // Starting RecyclerView with sent data
                        myExamsAdapter.setData(t.result.exams)
                    }
                }
            })
    }

    private fun deleteExam(exam: Exam, retries: Int = 5) {
        // Deleting the exam with the help of id
        myExamsViewModel.deleteExam(exam.id).asyncRequest()
            .subscribe(object : SingleObserver<ExamsMainResult> {
                override fun onSubscribe(d: Disposable) {
                    // Add the disposable to Composite Disposable
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    if (retries > 0) {
                        // Retry
                        deleteExam(exam, retries - 1)
                    } else {
                        // Error report to user
                        Snackbar.make(
                            binding.root, "خطا در دریافت اطلاعات", Snackbar.LENGTH_LONG
                        ).setAction(
                            "تلاش دوباره"
                        ) { deleteExam(exam) }.show()
                    }
                }

                override fun onSuccess(t: ExamsMainResult) {
                    if (t.status == 200) {
                        makeShortToast(this@MyExamsActivity, "آزمون با موفقیت حذف شد")
                        initData()
                    } else {
                        Snackbar.make(
                            binding.root,
                            "در هنگام حذف کردن آزمون خطایی رخ داد!",
                            Snackbar.LENGTH_LONG
                        ).setAction(
                            "تلاش دوباره"
                        ) { deleteExam(exam) }.show()
                    }
                }
            })
    }

    private fun showDeleteDialog(exam: Exam) {
        // Displaying the delete dialog
        val dialog = AlertDialog.Builder(this).create()

        val dialogBinding = DialogDeleteExamBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)

        dialogBinding.tvDeleteExam.text = "حذف ${exam.name} ؟"

        dialog.show()

        dialogBinding.btCancelDeleteExam.setOnClickListener {
            // If the cancel button is pressed, the dialog will be closed
            dialog.dismiss()
        }
        dialogBinding.btDeleteExam.setOnClickListener {
            dialog.dismiss()
            deleteExam(exam)
        }
    }

    override fun onExamClick(exam: Exam) {
        // One of the exams has been clicked
        val intent = Intent(this, ExamDetailActivity::class.java)
        intent.putExtra(SEND_SELECTED_EXAM_TO_EXAM_DETAIL_PAGE_KEY, exam)
        startActivity(intent)
    }

    private fun showToggleVisibilityBottomSheet(exam: Exam) {
        val bundle = Bundle()

        // put data to bundle
        bundle.putParcelable(SEND_SELECTED_EXAM_TO_TOGGLE_VISIBILITY_BOTTOM_SHEET_KEY, exam)

        toggleVisibilityBottomSheet.arguments = bundle

        // Display Toggle Visibility Bottom Sheet
        toggleVisibilityBottomSheet.show(supportFragmentManager, null)
    }

    override fun onEditExam(exam: Exam) {
        // TODO
        makeShortToast(this, "این بخش در حال توسعه است. با تشکر از شکیبایی شما")
    }

    override fun onDeleteExam(exam: Exam) {
        showDeleteDialog(exam)
    }

    override fun onToggleVisibility(exam: Exam) {
        showToggleVisibilityBottomSheet(exam)
    }
}