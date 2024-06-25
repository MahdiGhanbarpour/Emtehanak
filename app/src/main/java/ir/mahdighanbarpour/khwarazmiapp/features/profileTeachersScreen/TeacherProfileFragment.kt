package ir.mahdighanbarpour.khwarazmiapp.features.profileTeachersScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentTeacherProfileBinding

class TeacherProfileFragment : Fragment() {

    private lateinit var binding: FragmentTeacherProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}