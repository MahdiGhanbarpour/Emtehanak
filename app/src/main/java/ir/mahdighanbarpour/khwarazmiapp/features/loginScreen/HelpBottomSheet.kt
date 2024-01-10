package ir.mahdighanbarpour.khwarazmiapp.features.loginScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.BottomSheetHelpBinding
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT

class HelpBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetHelpBinding

    private var selectedRole = STUDENT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetHelpBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedRole = requireArguments().getString(SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY)!!

        if (selectedRole == STUDENT) {
            binding.btCancelHelp.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.blue)
            )
        } else {
            binding.btCancelHelp.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.teacher_color)
            )
        }

        listener()
    }

    private fun listener() {
        binding.btCancelHelp.setOnClickListener {
            //Close Help Bottom Sheet
            dismiss()
        }
    }
}