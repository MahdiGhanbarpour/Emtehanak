package ir.mahdighanbarpour.khwarazmiapp.features.shared

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.BottomSheetHelpBinding
import ir.mahdighanbarpour.khwarazmiapp.databinding.DialogContactSupportBinding
import ir.mahdighanbarpour.khwarazmiapp.features.frequentlyQuestionsScreen.FrequentlyQuestionsActivity
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.TEACHER
import ir.mahdighanbarpour.khwarazmiapp.utils.makeCall
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast

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
                ContextCompat.getColor(requireContext(), R.color.student_color)
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
        binding.cardViewContactSupport.setOnClickListener {
            showContactSupportDialog()
        }
        binding.cardViewFrequentlyQuestions.setOnClickListener {
            val intent = Intent(requireContext(), FrequentlyQuestionsActivity::class.java)
            intent.putExtra(
                SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY, requireArguments().getString(
                    SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY
                )
            )

            startActivity(intent)
        }
        binding.cardViewSupport.setOnClickListener {
            makeShortToast(requireContext(), "بخش ارتباط برخط با پشتیبانی در حال توسعه است. با تشکر از شکیبایی شما")
            //TODO
        }
    }

    private fun showContactSupportDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()

        val dialogBinding = DialogContactSupportBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)

        dialogBinding.btCloseContactSupport.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                if (selectedRole == TEACHER) R.color.teacher_color else R.color.student_color
            )
        )

        dialog.show()

        dialogBinding.btCloseContactSupport.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.tvSupportNum1.setOnClickListener {
            makeCall(requireContext(), dialogBinding.tvSupportNum1.text.toString())
        }

        dialogBinding.tvSupportNum2.setOnClickListener {
            makeCall(requireContext(), dialogBinding.tvSupportNum2.text.toString())
        }

        dialogBinding.tvSupportNum3.setOnClickListener {
            makeCall(requireContext(), dialogBinding.tvSupportNum3.text.toString())
        }
    }
}