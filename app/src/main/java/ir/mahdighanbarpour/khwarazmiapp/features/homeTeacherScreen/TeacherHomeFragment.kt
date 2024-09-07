package ir.mahdighanbarpour.khwarazmiapp.features.homeTeacherScreen

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
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentTeacherHomeBinding
import ir.mahdighanbarpour.khwarazmiapp.features.addExamScreen.AddExamActivity
import ir.mahdighanbarpour.khwarazmiapp.features.examDetailScreen.ExamDetailActivity
import ir.mahdighanbarpour.khwarazmiapp.features.examsListScreen.ExamsListActivity
import ir.mahdighanbarpour.khwarazmiapp.features.mainTeacherScreen.TeacherMainActivity
import ir.mahdighanbarpour.khwarazmiapp.features.myExamsScreen.MyExamsActivity
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.ExperiencedTeachersAdapter
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.PopularExamAdapter
import ir.mahdighanbarpour.khwarazmiapp.model.data.Exam
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.model.data.SliderItem
import ir.mahdighanbarpour.khwarazmiapp.model.data.SliderMainResult
import ir.mahdighanbarpour.khwarazmiapp.utils.MEDIA_BASE_URL
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_TO_EXAM_DETAIL_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_GRADE
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.changeStatusBarColor
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

        onFragmentShowed()
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
        binding.linearLayoutBottomEndItemTeacherMain.setOnClickListener {
            val intent = Intent(requireContext(), AddExamActivity::class.java)
            startActivity(intent)
        }
        binding.linearLayoutBottomStartItemTeacherMain.setOnClickListener {
            val intent = Intent(requireContext(), MyExamsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initData() {
        // Checking if the user has internet or not
        if (isInternetAvailable(requireContext())) {
            binding.ivErrorPopularExamsTeacherMain.visibility = View.GONE
            binding.recyclerPopularExamsTeacherMain.visibility = View.VISIBLE

            // If the snack bar is displayed, it will be hidden
            if (this::snackbar.isInitialized) {
                snackbar.dismiss()
            }

            playExamsLoadingAnim()
            getPopularExams()

            playSliderLoadingAnim()
            getSliderItems()
        } else {
            // Display the error to the user
            binding.ivErrorPopularExamsTeacherMain.visibility = View.VISIBLE
            binding.recyclerPopularExamsTeacherMain.visibility = View.INVISIBLE

            setSlider(arrayListOf())

            snackbar = Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "عدم دسترسی به اینترنت",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("تلاش مجدد") { initData() }
            snackbar.show()
        }
    }

    fun onFragmentShowed() {
        changeStatusBarColor(requireActivity().window, "#FFFFFFFF", true)
    }

    private fun getPopularExams(retries: Int = 5) {
        // Getting the list of popular exams from the server based on the user's grade
        teacherHomeViewModel.getPopularExams(
            "", sharedPreferences.getString(USER_GRADE, null)!!, "5"
        ).asyncRequest().subscribe(object : SingleObserver<ExamsMainResult> {
            override fun onSubscribe(d: Disposable) {
                // Add the disposable to Composite Disposable
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable) {
                if (retries > 0) {
                    // Retry
                    playExamsLoadingAnim()
                    getPopularExams(retries - 1)
                } else {
                    // Error report to user
                    binding.ivErrorPopularExamsTeacherMain.visibility = View.VISIBLE
                    binding.recyclerPopularExamsTeacherMain.visibility = View.INVISIBLE

                    snackbar = Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        "خطا در دریافت اطلاعات",
                        Snackbar.LENGTH_LONG
                    ).setAction(
                        "تلاش دوباره"
                    ) {
                        getPopularExams()
                    }
                    snackbar.show()
                }
            }

            override fun onSuccess(t: ExamsMainResult) {
                // Checking if exams have been found for the submitted grade
                if (t.result.exams.isEmpty()) {
                    binding.cardViewPopularExamsTeacherMain.visibility = View.GONE
                    binding.recyclerPopularExamsTeacherMain.visibility = View.GONE

                    val param =
                        binding.cardViewExperiencedTeachersTeacherMain.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(42, 48, 42, 0)
                    binding.cardViewExperiencedTeachersTeacherMain.layoutParams = param
                } else {
                    // Starting RecyclerView with sent data
                    binding.ivErrorPopularExamsTeacherMain.visibility = View.GONE
                    binding.recyclerPopularExamsTeacherMain.visibility = View.VISIBLE

                    initPopularExamRecycler(t.result.exams)
                }
            }
        })
    }

    private fun playExamsLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(teacherHomeViewModel.isPopularExamsDataLoading.subscribe {
            requireActivity().runOnUiThread {
                if (it) {
                    binding.animationViewPopularExamsTeacherMain.visibility = View.VISIBLE

                    binding.ivErrorPopularExamsTeacherMain.visibility = View.GONE
                    binding.recyclerPopularExamsTeacherMain.visibility = View.GONE

                    binding.animationViewPopularExamsTeacherMain.playAnimation()
                } else {
                    binding.animationViewPopularExamsTeacherMain.visibility = View.GONE

                    binding.animationViewPopularExamsTeacherMain.pauseAnimation()
                }
            }
        })
    }

    private fun playSliderLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(teacherHomeViewModel.isSliderDataLoading.subscribe {
            requireActivity().runOnUiThread {
                if (it) {
                    binding.animationViewSliderTeacherMain.visibility = View.VISIBLE
                    binding.sliderMainTeacher.visibility = View.GONE

                    binding.animationViewSliderTeacherMain.playAnimation()
                } else {
                    binding.animationViewSliderTeacherMain.visibility = View.GONE
                    binding.sliderMainTeacher.visibility = View.VISIBLE

                    binding.animationViewSliderTeacherMain.pauseAnimation()
                }
            }
        })
    }

    private fun getSliderItems(retries: Int = 5) {
        // Getting the list of slider items from the server
        teacherHomeViewModel.getSliderItems("0").asyncRequest()
            .subscribe(object : SingleObserver<SliderMainResult> {
                override fun onSubscribe(d: Disposable) {
                    // Add the disposable to Composite Disposable
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    if (retries > 0) {
                        // Retry
                        playSliderLoadingAnim()
                        getSliderItems(retries - 1)
                    } else {
                        // Error report to user
                        setSlider(arrayListOf())
                    }
                }

                override fun onSuccess(t: SliderMainResult) {
                    setSlider(t.result.sliderItems)
                }
            })
    }

    private fun setSlider(data: List<SliderItem>) {
        // Setting up data and launch slider
        val imageList = arrayListOf<SlideModel>()
        if (data.isNotEmpty()) {
            for (item in data) {
                imageList.add(SlideModel(MEDIA_BASE_URL + item.image, null, ScaleTypes.CENTER_CROP))
            }
        } else {
            // Error report to user
            imageList.add(SlideModel(R.drawable.img_slider, null, ScaleTypes.CENTER_CROP))
        }

        binding.sliderMainTeacher.setImageList(imageList)
    }

    private fun initPopularExamRecycler(data: List<Exam>) {
        // Making the adapter and making the necessary settings
        popularExamAdapter = PopularExamAdapter(data, this)
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