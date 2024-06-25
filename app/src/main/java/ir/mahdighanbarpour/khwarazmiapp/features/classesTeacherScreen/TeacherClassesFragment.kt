package ir.mahdighanbarpour.khwarazmiapp.features.classesTeacherScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentTeacherClassesBinding

class TeacherClassesFragment : Fragment() {

    private lateinit var binding:FragmentTeacherClassesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherClassesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}