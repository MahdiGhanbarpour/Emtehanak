package ir.mahdighanbarpour.khwarazmiapp.features.profileStudentScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentStudentProfileBinding

class StudentProfileFragment : Fragment() {

    private lateinit var binding: FragmentStudentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentProfileBinding.inflate(layoutInflater)
        return binding.root
    }
}