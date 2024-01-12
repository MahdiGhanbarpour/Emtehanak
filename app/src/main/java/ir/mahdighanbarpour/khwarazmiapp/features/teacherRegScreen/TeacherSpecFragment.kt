package ir.mahdighanbarpour.khwarazmiapp.features.teacherRegScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentTeacherSpecBinding
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast

class TeacherSpecFragment : Fragment() {

    private lateinit var binding: FragmentTeacherSpecBinding

    private val expertiseItems = listOf(
        "علوم تجربی",
        "ریاضی",
        "ادبیات فارسی",
        "زبان انگلیسی",
        "دینی",
        "آمادگی دفاعی",
        "زبان عربی",
        "مطالعات اجتماعی",
        "هنر",
        "کار و فناوری"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherSpecBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setExpertiseAdapter()
    }

    private fun setExpertiseAdapter() {
        val expertiseAdapter =
            ArrayAdapter(requireContext(), R.layout.layout_drop_down_items, expertiseItems)
        binding.tvTeacherExpertiseReg.setAdapter(expertiseAdapter)
    }

    fun checkInputs() {
        val enteredExpertise = binding.tvTeacherExpertiseReg.text.toString()
        val enteredActivityYear = binding.etActivityYearReg.text.toString()

        var isEnteredExpertiseOk = false
        var isEnteredActivityYearOk = false

        if (enteredExpertise.isEmpty()) {
            binding.etLayoutTeacherExpertiseReg.error = "رشته مورد تدریس خود را وارد کنید"
        } else if (enteredExpertise !in expertiseItems) {
            binding.etLayoutTeacherExpertiseReg.error = "رشته مورد تدریس معتبر نمی باشد"
        } else {
            binding.etLayoutTeacherExpertiseReg.isErrorEnabled = false
            isEnteredExpertiseOk = true
        }

        if (enteredActivityYear.isEmpty()) {
            binding.etLayoutActivityYearReg.error = "سال شروع به فعالیت خود را وارد کنید"
        } else if (enteredActivityYear.toInt() !in 1300..1402) {
            binding.etLayoutActivityYearReg.error = "سال شروع به فعالیت معتبر نمی باشد"
        } else {
            binding.etLayoutActivityYearReg.isErrorEnabled = false
            isEnteredActivityYearOk = true
        }

        if (isEnteredExpertiseOk && isEnteredActivityYearOk) {
            makeShortToast(requireContext(), "همه اطلاعات صحیح است")
        }
    }
}