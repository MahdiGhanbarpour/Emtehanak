package ir.mahdighanbarpour.khwarazmiapp.features.regScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentRegisterBinding
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private val monthItems = listOf(
        "فروردین",
        "اردیبهشت",
        "خرداد",
        "تیر",
        "مرداد",
        "شهریور",
        "مهر",
        "آبان",
        "آذر",
        "دی",
        "بهمن",
        "اسفند"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBirthdayMonthAdapter()
    }

    private fun setBirthdayMonthAdapter() {
        val monthAdapter =
            ArrayAdapter(requireContext(), R.layout.layout_drop_down_items, monthItems)
        binding.tvMonthReg.setAdapter(monthAdapter)
    }

    fun checkInputs() {
        val enteredName = binding.etFullNameReg.text.toString()
        val enteredDay = binding.etDayReg.text.toString()
        val enteredMonth = binding.tvMonthReg.text.toString()
        val enteredYear = binding.etYearReg.text.toString()

        if (enteredName.isEmpty()) {
            binding.etLayoutFullNameReg.error = "نام و نام خانوادگی خود را وارد کنید"
        } else if (enteredName.length <= 5) {
            binding.etLayoutFullNameReg.error = "نام و نام خانوادگی معتبر نمی باشد"
        } else {
            binding.etLayoutFullNameReg.isErrorEnabled = false
        }

        if (checkEnteredDay(enteredDay)) {
            if (checkEnteredMonth(enteredDay, enteredMonth)) {
                if (checkEnteredYear(enteredYear)) {
                    makeShortToast(requireContext(), "همه اطلاعات صحیح است")
                }
            }
        }
    }

    private fun checkEnteredDay(day: String): Boolean {
        return if (day.isEmpty()) {
            binding.etLayoutDayReg.error = ""
            makeShortToast(requireContext(), "روز تولد خود را وارد کنید")

            false
        } else if (day.toInt() !in 1..31) {
            binding.etLayoutDayReg.error = ""
            makeShortToast(requireContext(), "روز تولد وارد شده معتبر نیست")

            false
        } else {
            binding.etLayoutDayReg.isErrorEnabled = false
            true
        }
    }

    private fun checkEnteredMonth(day: String, month: String): Boolean {
        return if (month.isEmpty()) {
            binding.etLayoutMonthReg.error = ""
            makeShortToast(requireContext(), "ماه تولد خود را وارد کنید")

            false
        } else if (month !in monthItems) {
            binding.etLayoutMonthReg.error = ""
            makeShortToast(requireContext(), "ماه تولد وارد شده معتبر نیست")

            false
        } else {
            binding.etLayoutMonthReg.isErrorEnabled = false
            checkEnteredDayByMonth(day, month)
        }
    }

    private fun checkEnteredDayByMonth(day: String, month: String): Boolean {
        return when (month) {
            "اسفند" -> {
                if (day.toInt() > 30) {
                    binding.etLayoutDayReg.error = ""
                    makeShortToast(requireContext(), "روز تولد وارد شده معتبر نیست")
                    false
                } else {
                    true
                }
            }

            else -> true
        }
    }

    private fun checkEnteredYear(year: String): Boolean {
        return if (year.isEmpty()) {
            binding.etLayoutYearReg.error = ""
            makeShortToast(requireContext(), "سال تولد خود را وارد کنید")

            false
        } else if (year.toInt() !in 1300..1402) {
            binding.etLayoutYearReg.error = ""
            makeShortToast(requireContext(), "سال تولد وارد شده معتبر نیست")

            false
        } else {
            binding.etLayoutYearReg.isErrorEnabled = false
            true
        }
    }
}