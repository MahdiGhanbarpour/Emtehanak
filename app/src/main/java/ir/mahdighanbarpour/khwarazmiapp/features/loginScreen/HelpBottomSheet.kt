package ir.mahdighanbarpour.khwarazmiapp.features.loginScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.mahdighanbarpour.khwarazmiapp.databinding.BottomSheetHelpBinding

class HelpBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetHelpBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener()
    }

    private fun listener() {
        binding.btCancelHelp.setOnClickListener {
            //Close Help Bottom Sheet
            dismiss()
        }
    }
}