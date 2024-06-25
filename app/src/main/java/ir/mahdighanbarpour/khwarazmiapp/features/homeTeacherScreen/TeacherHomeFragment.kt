package ir.mahdighanbarpour.khwarazmiapp.features.homeTeacherScreen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentTeacherHomeBinding
import ir.mahdighanbarpour.khwarazmiapp.features.examDetailScreen.ExamDetailActivity
import ir.mahdighanbarpour.khwarazmiapp.features.examListScreen.ExamsListActivity
import ir.mahdighanbarpour.khwarazmiapp.features.mainTeacherScreen.TeacherMainActivity
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.ExperiencedTeachersAdapter
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.PopularExamAdapter
import ir.mahdighanbarpour.khwarazmiapp.model.data.Exam
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsResult
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_TO_EXAM_DETAIL_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_GRADE
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TeacherHomeFragment : Fragment(), PopularExamAdapter.PopularExamEvents,
    ExperiencedTeachersAdapter.ExperiencedTeachersEvents {

    private lateinit var binding: FragmentTeacherHomeBinding
    private lateinit var experiencedTeachersAdapter: ExperiencedTeachersAdapter
    private lateinit var popularExamAdapter: PopularExamAdapter
    private lateinit var snackbar: Snackbar

    private val teacherHomeViewModel: TeacherHomeViewModel by viewModel()
    private val sharedPreferences: SharedPreferences by inject()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSlider()
        initExperiencedTeachersRecycler()
        initPopularExamRecycler()

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
        binding.cardViewOpenDrawerTeacherMain.setOnClickListener {
            // If the button is pressed, it will open the NavigationDrawer
            (requireActivity() as TeacherMainActivity).openDrawer()
        }
        binding.swipeRefreshTeacherMain.setOnRefreshListener {
            // Refresh data
            initData()

            // Stop rotating refresh view after 1500ms
            Handler(Looper.myLooper()!!).postDelayed({
                binding.swipeRefreshTeacherMain.isRefreshing = false
            }, 1500)
        }
        binding.linearLayoutTopStartItemTeacherMain.setOnClickListener {
            val intent = Intent(requireContext(), ExamsListActivity::class.java)
            startActivity(intent)
        }
        binding.ivMorePopularExamsTeacherMain.setOnClickListener {
            val intent = Intent(requireContext(), ExamsListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initData() {
        // Checking if the user has internet or not
        if (isInternetAvailable(requireContext())) {
            binding.ivErrorPopularExamsTeacherMain.visibility = View.GONE

            // If the snack bar is displayed, it will be hidden
            if (this::snackbar.isInitialized) {
                snackbar.dismiss()
            }

            playLoadingAnim()
            getPopularExams()
        } else {
            // Display the error to the user
            binding.ivErrorPopularExamsTeacherMain.visibility = View.VISIBLE

            popularExamAdapter.setData(arrayListOf())

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
        teacherHomeViewModel.getPopularExams(
            "", sharedPreferences.getString(USER_GRADE, null)!!, "5"
        ).asyncRequest().subscribe(object : SingleObserver<ExamsMainResult> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    // Error report to user
                    binding.ivErrorPopularExamsTeacherMain.visibility = View.VISIBLE

                    snackbar = Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        "خطا در دریافت اطلاعات",
                        Snackbar.LENGTH_LONG
                    ).setAction(
                        "تلاش دوباره"
                    ) {
                        binding.ivErrorPopularExamsTeacherMain.visibility = View.GONE
                        getPopularExams()
                    }
                    snackbar.show()
                }

                override fun onSuccess(t: ExamsMainResult) {
                    // Checking if exams have been found for the submitted grade
                    if (t.result.exams.isEmpty()) {
                        binding.cardViewPopularExamsTeacherMain.visibility = View.GONE
                        binding.recyclerPopularExamsTeacherMain.visibility = View.GONE
                    } else {
                        // Starting RecyclerView with sent data
                        setPopularExamRecyclerData(t.result)
                    }
                }
            })
    }

    private fun playLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(teacherHomeViewModel.isPopularExamsDataLoading.subscribe {
            requireActivity().runOnUiThread {
                if (it) {
                    binding.animationViewPopularExamsTeacherMain.visibility = View.VISIBLE

                    binding.animationViewPopularExamsTeacherMain.playAnimation()
                } else {
                    binding.animationViewPopularExamsTeacherMain.visibility = View.GONE

                    binding.animationViewPopularExamsTeacherMain.pauseAnimation()
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

        binding.sliderMainTeacher.setImageList(imageList)
    }

    private fun initPopularExamRecycler() {
        // Making the adapter and making the necessary settings
        popularExamAdapter = PopularExamAdapter(arrayListOf(), this)
        binding.recyclerPopularExamsTeacherMain.adapter = popularExamAdapter

        binding.recyclerPopularExamsTeacherMain.layoutManager =
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
        binding.recyclerExperiencedTeachersTeacherMain.adapter = experiencedTeachersAdapter

        binding.recyclerExperiencedTeachersTeacherMain.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    private fun setPopularExamRecyclerData(data: ExamsResult) {
        popularExamAdapter.setData(data.exams)
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