package ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentStudentHomeBinding

class StudentHomeFragment : Fragment() {

    private lateinit var binding: FragmentStudentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentHomeBinding.inflate(layoutInflater)
        return binding.root
    }
}