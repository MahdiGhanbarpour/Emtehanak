package ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentStudentHomeBinding
import ir.mahdighanbarpour.khwarazmiapp.features.examDetailScreen.ExamDetailActivity
import ir.mahdighanbarpour.khwarazmiapp.features.examListScreen.ExamsListActivity
import ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen.adapters.LessonsAdapter
import ir.mahdighanbarpour.khwarazmiapp.features.lessonScreen.LessonActivity
import ir.mahdighanbarpour.khwarazmiapp.features.mainStudentScreen.StudentMainActivity
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.ExperiencedTeachersAdapter
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.PopularExamAdapter
import ir.mahdighanbarpour.khwarazmiapp.model.data.Exam
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.Lesson
import ir.mahdighanbarpour.khwarazmiapp.model.data.LessonsMainResult
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_TO_EXAM_DETAIL_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_LESSON_TO_LESSON_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_GRADE
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudentHomeFragment : Fragment(), LessonsAdapter.LessonEvents,
    PopularExamAdapter.PopularExamEvents, ExperiencedTeachersAdapter.ExperiencedTeachersEvents {

    private lateinit var binding: FragmentStudentHomeBinding
    private lateinit var lessonsAdapter: LessonsAdapter
    private lateinit var experiencedTeachersAdapter: ExperiencedTeachersAdapter
    private lateinit var popularExamAdapter: PopularExamAdapter
    private lateinit var snackbar: Snackbar

    private val studentHomeViewModel: StudentHomeViewModel by viewModel()
    private val sharedPreferences: SharedPreferences by inject()

    private val compositeDisposable = CompositeDisposable()
    private val grade = sharedPreferences.getString(USER_GRADE, null)!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSlider()
        initLessonRecycler(arrayListOf())
        initExperiencedTeachersRecycler()
        initPopularExamRecycler(arrayListOf())

        initData()

        listener()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Clear the values in Composite Disposable
        compositeDisposable.clear()

        // If the snack bar is displayed, it will be hidden
        if (this::snackbar.isInitialized) {
            snackbar.dismiss()
        }
    }


    private fun listener() {
        binding.cardViewOpenDrawerStudentMain.setOnClickListener {
            // If the button is pressed, it will open the NavigationDrawer
            (requireActivity() as StudentMainActivity).openDrawer()
        }
        binding.swipeRefreshStudentMain.setOnRefreshListener {
            // Refresh data
            initData()

            // Stop rotating refresh view after 1500ms
            Handler(Looper.myLooper()!!).postDelayed({
                binding.swipeRefreshStudentMain.isRefreshing = false
            }, 1500)
        }
        binding.linearLayoutTopStartItemStudentMain.setOnClickListener {
            // If the linear layout is clicked, the page will be opened
            val intent = Intent(requireContext(), ExamsListActivity::class.java)
            startActivity(intent)
        }
        binding.ivMorePopularExamsStudentMain.setOnClickListener {
            // If the button is clicked, the page will be opened
            val intent = Intent(requireContext(), ExamsListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initData() {
        // Checking if the user has internet or not
        if (isInternetAvailable(requireContext())) {
            binding.ivErrorPopularExamsStudentMain.visibility = View.GONE
            binding.recyclerPopularExamsStudentMain.visibility = View.VISIBLE

            binding.ivErrorLessonsStudentMain.visibility = View.GONE
            binding.recyclerLessonsStudentMain.visibility = View.VISIBLE

            // If the snack bar is displayed, it will be hidden
            if (this::snackbar.isInitialized) {
                snackbar.dismiss()
            }

            playPopularExamLoadingAnim()
            getPopularExams()

            playLessonLoadingAnim()
            getLessons()
        } else {
            // Display the error to the user
            binding.ivErrorPopularExamsStudentMain.visibility = View.VISIBLE
            binding.recyclerPopularExamsStudentMain.visibility = View.INVISIBLE

            binding.ivErrorLessonsStudentMain.visibility = View.VISIBLE
            binding.recyclerLessonsStudentMain.visibility = View.INVISIBLE

            snackbar = Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "عدم دسترسی به اینترنت",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("تلاش مجدد") { initData() }
            snackbar.show()
        }
    }

    private fun getPopularExams() {
        // Getting the list of popular exams from the server based on the user's grade
        studentHomeViewModel.getPopularExams(grade, "", "5").asyncRequest()
            .subscribe(object : SingleObserver<ExamsMainResult> {
                override fun onSubscribe(d: Disposable) {
                    // Add the Disposable to the CompositeDisposable
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    // Error report to user
                    binding.ivErrorPopularExamsStudentMain.visibility = View.VISIBLE

                    snackbar = Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        "خطا در دریافت اطلاعات",
                        Snackbar.LENGTH_LONG
                    ).setAction(
                        "تلاش دوباره"
                    ) {
                        binding.ivErrorPopularExamsStudentMain.visibility = View.GONE
                        getPopularExams()
                    }
                    snackbar.show()
                }

                override fun onSuccess(t: ExamsMainResult) {
                    // Checking if exams have been found for the submitted grade
                    if (t.result.exams.isEmpty()) {
                        binding.cardViewPopularExamsStudentMain.visibility = View.GONE
                        binding.recyclerPopularExamsStudentMain.visibility = View.GONE
                    } else {
                        // Starting RecyclerView with sent data
                        initPopularExamRecycler(t.result.exams)
                    }
                }
            })
    }

    private fun playPopularExamLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(studentHomeViewModel.isPopularExamsDataLoading.subscribe {
            requireActivity().runOnUiThread {
                if (it) {
                    binding.animationViewPopularExamsStudentMain.visibility = View.VISIBLE

                    binding.animationViewPopularExamsStudentMain.playAnimation()
                } else {
                    binding.animationViewPopularExamsStudentMain.visibility = View.GONE

                    binding.animationViewPopularExamsStudentMain.pauseAnimation()
                }
            }
        })
    }

    private fun getLessons() {
        // Getting the list of lessons from the server based on the user's grade
        studentHomeViewModel.getLessons(grade, "5").asyncRequest()
            .subscribe(object : SingleObserver<LessonsMainResult> {
                override fun onSubscribe(d: Disposable) {
                    // Add the Disposable to the CompositeDisposable
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    // Error report to user
                    binding.ivErrorLessonsStudentMain.visibility = View.VISIBLE

                    snackbar = Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        "خطا در دریافت اطلاعات",
                        Snackbar.LENGTH_LONG
                    ).setAction(
                        "تلاش دوباره"
                    ) {
                        binding.ivErrorLessonsStudentMain.visibility = View.GONE
                        getPopularExams()
                    }
                    snackbar.show()
                }

                override fun onSuccess(t: LessonsMainResult) {
                    // Checking if exams have been found for the submitted grade
                    if (t.lessonsResult.lessons.isEmpty()) {
                        binding.cardViewLessonsStudentMain.visibility = View.GONE
                        binding.recyclerLessonsStudentMain.visibility = View.GONE
                    } else {
                        // Starting RecyclerView with sent data
                        initLessonRecycler(t.lessonsResult.lessons)
                    }
                }
            })
    }

    private fun playLessonLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(studentHomeViewModel.isLessonsDataLoading.subscribe {
            requireActivity().runOnUiThread {
                if (it) {
                    binding.animationViewLessonsStudentMain.visibility = View.VISIBLE

                    binding.animationViewLessonsStudentMain.playAnimation()
                } else {
                    binding.animationViewLessonsStudentMain.visibility = View.GONE

                    binding.animationViewLessonsStudentMain.pauseAnimation()
                }
            }
        })
    }

    private fun initSlider() {
        // Setting up data and launch slider
        val imageList = arrayListOf(
            SlideModel(
                "https://www.tasvirezendegi.com/wp-content/uploads/2023/03/Sampad.jpg",
                null,
                ScaleTypes.CENTER_CROP
            ),
            SlideModel(
                "https://goftemanezagros.ir/my_content/uploads/2022/01/DC09F75E-4280-4C5C-8C9A-992FED5B5810-1100x700.png",
                null,
                ScaleTypes.CENTER_CROP
            ),
            SlideModel(
                "https://afallahi.ir/wp-content/uploads/2020/11/logo_vezarat.jpg",
                null,
                ScaleTypes.CENTER_CROP
            ),
        )

        binding.sliderMain.setImageList(imageList)
    }

    private fun initLessonRecycler(data: List<Lesson>) {
        // Making the adapter and making the necessary settings
        lessonsAdapter = LessonsAdapter(data, this)
        binding.recyclerLessonsStudentMain.adapter = lessonsAdapter

        binding.recyclerLessonsStudentMain.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    private fun initPopularExamRecycler(data: List<Exam>) {
        // Making the adapter and making the necessary settings
        popularExamAdapter = PopularExamAdapter(data, this)
        binding.recyclerPopularExamsStudentMain.adapter = popularExamAdapter

        binding.recyclerPopularExamsStudentMain.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    private fun initExperiencedTeachersRecycler() {
        // Making the adapter and making the necessary settings
        val data = arrayListOf(
            Triple(
                "علی زمردیان", "کار و فناوری", null
            ),
            Triple(
                "کاظم نیری",
                "علوم تجربی",
                "https://cdn.pana.ir/Media/Image/1402/01/11/638158359306262261.jpg"
            ),
            Triple(
                "سجاد غانمی زاده", "کار و فناوری - هنر", null
            ),
            Triple(
                "سجاد طبیب نژاد", "علوم تجربی", null
            ),
        )

        experiencedTeachersAdapter = ExperiencedTeachersAdapter(data, this)
        binding.recyclerExperiencedTeachersStudentMain.adapter = experiencedTeachersAdapter

        binding.recyclerExperiencedTeachersStudentMain.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    override fun onLessonClicked(data: Lesson) {
        // One of the courses has been clicked
        val intent = Intent(requireActivity(), LessonActivity::class.java)
        intent.putExtra(SEND_SELECTED_LESSON_TO_LESSON_PAGE_KEY, data)
        startActivity(intent)
    }

    override fun onPopularExamClicked(data: Exam) {
        // One of the exams has been clicked
        val intent = Intent(requireActivity(), ExamDetailActivity::class.java)
        intent.putExtra(SEND_SELECTED_EXAM_TO_EXAM_DETAIL_PAGE_KEY, data)
        startActivity(intent)
    }

    override fun onExperiencedTeachersClicked(data: Triple<String, String, String?>) {
        // One of the teachers has been clicked
        // TODO
        makeShortToast(requireContext(), "این بخش در حال توسعه است. با تشکر از شکیبایی شما")
    }
}