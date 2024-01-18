package ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentStudentHomeBinding
import ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen.adapters.CoursesAdapter
import ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen.adapters.ExperiencedTeachersAdapter
import ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen.adapters.PopularExamAdapter
import ir.mahdighanbarpour.khwarazmiapp.features.mainStudentScreen.StudentMainActivity
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast

class StudentHomeFragment : Fragment(), CoursesAdapter.CourseEvents,
    PopularExamAdapter.PopularExamEvents, ExperiencedTeachersAdapter.ExperiencedTeachersEvents {

    private lateinit var binding: FragmentStudentHomeBinding
    private lateinit var coursesAdapter: CoursesAdapter
    private lateinit var experiencedTeachersAdapter: ExperiencedTeachersAdapter
    private lateinit var popularExamAdapter: PopularExamAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSlider()
        initCourseRecycler()
        initPopularExamRecycler()
        initExperiencedTeachersRecycler()

        listener()
    }

    private fun listener() {
        binding.cardViewOpenDrawerStudentMain.setOnClickListener {
            (requireActivity() as StudentMainActivity).openDrawer()
        }
    }

    private fun initSlider() {
        // Set up data and launch slider
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

    private fun initCourseRecycler() {
        // Set up data and launch recycler
        val data = arrayListOf(
            Pair(
                "علوم تجربی",
                "http://www.chap.sch.ir/sites/default/files/styles/image_node_book/public/book_image/1402-1403/C906.jpg"
            ),
            Pair(
                "ریاضی",
                "http://www.chap.sch.ir/sites/default/files/styles/image_node_book/public/book_image/1402-1403/C905.jpg"
            ),
            Pair(
                "فارسی",
                "http://www.chap.sch.ir/sites/default/files/styles/image_node_book/public/book_image/1402-1403/C903.jpg"
            ),
            Pair(
                "مطالعات اجتماعی",
                "http://www.chap.sch.ir/sites/default/files/styles/image_node_book/public/book_image/1402-1403/C907.jpg"
            ),
            Pair(
                "عربی",
                "http://www.chap.sch.ir/sites/default/files/styles/image_node_book/public/book_image/1402-1403/C909_0.jpg"
            ),
        )

        coursesAdapter = CoursesAdapter(data, this)
        binding.recyclerCoursesStudentMain.adapter = coursesAdapter

        binding.recyclerCoursesStudentMain.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    private fun initPopularExamRecycler() {
        // Set up data and launch recycler
        val data = arrayListOf(
            Pair(
                "آزمون ورودی نهم سمپاد 1400",
                "https://media.farsnews.ir/Uploaded/Files/Images/1399/12/21/13991221000563_Test_PhotoA.jpg"
            ),
            Pair(
                "آزمون ورودی نهم سمپاد 1398",
                "https://media.farsnews.ir/Uploaded/Files/Images/1399/12/21/13991221000563_Test_PhotoA.jpg"
            ),
            Pair(
                "آزمون نهایی نهم 1401", "https://ivnanews.ir/storage//photos/20/62c511b3f3bdf.jpg"
            ),
            Pair(
                "آزمون نهایی نهم 1398", "https://ivnanews.ir/storage//photos/20/62c511b3f3bdf.jpg"
            ),
            Pair(
                "آزمون نهایی نهم 1400", "https://ivnanews.ir/storage//photos/20/62c511b3f3bdf.jpg"
            ),
        )

        popularExamAdapter = PopularExamAdapter(data, this)
        binding.recyclerPopularExamsStudentMain.adapter = popularExamAdapter

        binding.recyclerPopularExamsStudentMain.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    private fun initExperiencedTeachersRecycler() {
        // Set up data and launch recycler
        val data = arrayListOf(
            Triple(
                "علی زمردیان", "کار و فناوری", null
            ),
            Triple(
                "کاظم نیری",
                "علوم تجربی",
                "https://gama.ir/uploads/user/avatars/teacherAvatar_82a89cc2afc8d08097ef56b62c668f99.jpg"
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

    override fun onCourseClicked(data: Pair<String, String>) {
        makeShortToast(requireContext(), "این بخش در حال توسعه است. با تشکر از شکیبایی شما")
    }

    override fun onPopularExamClicked(data: Pair<String, String>) {
        makeShortToast(requireContext(), "این بخش در حال توسعه است. با تشکر از شکیبایی شما")
    }

    override fun onExperiencedTeachersClicked(data: Triple<String, String, String?>) {
        makeShortToast(requireContext(), "این بخش در حال توسعه است. با تشکر از شکیبایی شما")
    }
}