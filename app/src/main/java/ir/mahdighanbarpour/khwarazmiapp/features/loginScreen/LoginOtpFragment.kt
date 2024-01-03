package ir.mahdighanbarpour.khwarazmiapp.features.loginScreen

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.FragmentLoginOtpBinding
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast


class LoginOtpFragment : Fragment() {

    private lateinit var binding: FragmentLoginOtpBinding

    private var editTextsList: ArrayList<EditText> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginOtpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextsList = arrayListOf(
            binding.etFirstOtp, binding.etSecondOtp, binding.etThirdOtp, binding.etFourthOtp
        )

        listener()
    }

    private fun listener() {
        binding.etFirstOtp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.etSecondOtp.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
            }
        })

        binding.etSecondOtp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.etThirdOtp.requestFocus()
                }
                if (s.isEmpty()) {
                    binding.etFirstOtp.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
            }
        })

        binding.etThirdOtp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.etFourthOtp.requestFocus()
                }
                if (s.isEmpty()) {
                    binding.etSecondOtp.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
            }
        })

        binding.etFourthOtp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()) {
                    binding.etThirdOtp.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
            }
        })
    }

    fun checkOtpCode() {
        val enteredOtpCode =
            binding.etFirstOtp.text.toString() + binding.etSecondOtp.text.toString() + binding.etThirdOtp.text.toString() + binding.etFourthOtp.text.toString()

        if (enteredOtpCode != "1234") {
            changeEditTextsColor(R.color.red)
            makeShortToast(requireContext(), "کد وارد شده معتبر نیست")
        } else {
            changeEditTextsColor(R.color.blue)
        }
    }

    private fun changeEditTextsColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            for (editTexts in editTextsList) {
                editTexts.background.colorFilter = BlendModeColorFilter(
                    ContextCompat.getColor(requireContext(), color), BlendMode.SRC_ATOP
                )
            }
        } else {
            for (editTexts in editTextsList) {
                editTexts.background.setColorFilter(
                    ContextCompat.getColor(requireContext(), color), PorterDuff.Mode.SRC_ATOP
                )
            }
        }
    }
}