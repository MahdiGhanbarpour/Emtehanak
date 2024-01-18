package ir.mahdighanbarpour.khwarazmiapp.features.teachersStudentScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentStudentTeachersBinding

class StudentTeachersFragment : Fragment() {

    private lateinit var binding: FragmentStudentTeachersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentTeachersBinding.inflate(layoutInflater)
        return binding.root
    }
}