package ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.mahdighanbarpour.khwarazmiapp.databinding.BottomSheetExitBinding

class ExitBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetExitBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetExitBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener()
    }

    private fun listener() {
        binding.btYes.setOnClickListener {
            // Close this bottom sheet and close the parent activity
            dismiss()
            requireActivity().finish()
        }
        binding.btNo.setOnClickListener {
            // Close this bottom sheet
            dismiss()
        }
        binding.ivCancel.setOnClickListener {
            // Close this bottom sheet
            dismiss()
        }
    }
}