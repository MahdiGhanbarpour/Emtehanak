package ir.mahdighanbarpour.emtehanak.features.classesTeacherScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.mahdighanbarpour.emtehanak.databinding.FragmentTeacherClassesBinding
import ir.mahdighanbarpour.emtehanak.utils.changeStatusBarColor

class TeacherClassesFragment : Fragment() {

    private lateinit var binding: FragmentTeacherClassesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherClassesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onFragmentShowed()
    }

    fun onFragmentShowed() {
        changeStatusBarColor(requireActivity().window, "#FFFFFFFF", true)
    }
}