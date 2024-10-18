package ir.mahdighanbarpour.emtehanak.features.profileTeachersScreen

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import ir.mahdighanbarpour.emtehanak.databinding.DialogLogoutBinding
import ir.mahdighanbarpour.emtehanak.databinding.FragmentTeacherProfileBinding
import ir.mahdighanbarpour.emtehanak.features.mainLoginScreen.LoginActivity
import ir.mahdighanbarpour.emtehanak.utils.IS_USER_LOGGED_IN
import ir.mahdighanbarpour.emtehanak.utils.USER_ACTIVITY_YEAR
import ir.mahdighanbarpour.emtehanak.utils.USER_FULL_NAME
import ir.mahdighanbarpour.emtehanak.utils.USER_GRADE
import ir.mahdighanbarpour.emtehanak.utils.USER_PHONE_NUM
import ir.mahdighanbarpour.emtehanak.utils.USER_ROLE
import ir.mahdighanbarpour.emtehanak.utils.USER_STUDY_FIELD
import ir.mahdighanbarpour.emtehanak.utils.changeStatusBarColor
import org.koin.android.ext.android.inject

class TeacherProfileFragment : Fragment() {

    private lateinit var binding: FragmentTeacherProfileBinding
    private lateinit var editor: Editor

    private val sharedPreferences: SharedPreferences by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editor = sharedPreferences.edit()

        onFragmentShowed()
        setData()
        listener()
    }

    private fun listener() {
        binding.ivLogout.setOnClickListener {
            showLogOutDialog()
        }
    }

    fun onFragmentShowed() {
        changeStatusBarColor(requireActivity().window, "#2d7de2", false)
    }

    private fun setData() {
        binding.tvNameTeacherProfile.text = sharedPreferences.getString(USER_FULL_NAME, "خطا")

        binding.tvFullNameValueTeacherProfile.text =
            sharedPreferences.getString(USER_FULL_NAME, "خطا")
        binding.tvPhoneNumberValueTeacherProfile.text =
            sharedPreferences.getString(USER_PHONE_NUM, "خطا")
        binding.tvStudyFieldValueTeacherProfile.text =
            sharedPreferences.getString(USER_STUDY_FIELD, "خطا")
        binding.tvActivityYearValueTeacherProfile.text =
            sharedPreferences.getString(USER_ACTIVITY_YEAR, "خطا")

        var grades = sharedPreferences.getString(USER_GRADE, "خطا")
        grades = grades!!.removeSurrounding("[", "]").split(",").joinToString(", ") { it.trim('"') }
        binding.tvGradesValueTeacherProfile.text = grades
    }

    private fun showLogOutDialog() {
        // Displaying the logout dialog
        val dialog = AlertDialog.Builder(requireContext()).create()

        val dialogBinding = DialogLogoutBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.btNoCancel.setOnClickListener {
            // If the cancel button is pressed, the dialog will be closed
            dialog.dismiss()
        }
        dialogBinding.btYesLogout.setOnClickListener {
            // If the exit button is pressed, the user's login data will be deleted and it will go to the login page
            editor.putBoolean(IS_USER_LOGGED_IN, false)
            editor.putString(USER_FULL_NAME, null)
            editor.putString(USER_PHONE_NUM, null)
            editor.putString(USER_GRADE, null)
            editor.putString(USER_ROLE, null)
            editor.putString(USER_STUDY_FIELD, null)
            editor.putString(USER_ACTIVITY_YEAR, null)
            editor.commit()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}