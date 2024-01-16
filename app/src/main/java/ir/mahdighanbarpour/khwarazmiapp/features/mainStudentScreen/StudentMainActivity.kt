package ir.mahdighanbarpour.khwarazmiapp.features.mainStudentScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityStudentMainBinding
import ir.mahdighanbarpour.khwarazmiapp.databinding.DialogLogoutBinding
import ir.mahdighanbarpour.khwarazmiapp.features.mainLoginScreen.LoginActivity
import ir.mahdighanbarpour.khwarazmiapp.features.termsScreen.TermsActivity
import ir.mahdighanbarpour.khwarazmiapp.utils.HelpBottomSheet
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT_MAIN

class StudentMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentMainBinding

    private val helpBottomSheet = HelpBottomSheet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener()
    }

    private fun listener() {
        binding.navViewStudentMain.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_logout -> {
                    showLogOutDialog()
                }

                R.id.menu_terms -> {
                    val intent = Intent(this, TermsActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_support -> {
                    showHelpBottomSheet()
                }
            }

            true
        }
    }

    private fun showLogOutDialog() {
        val dialog = AlertDialog.Builder(this).create()

        val dialogBinding = DialogLogoutBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.btNoCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btYesLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun showHelpBottomSheet() {
        if (!helpBottomSheet.isVisible) {
            val bundle = Bundle()

            bundle.putString(SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY, STUDENT)
            bundle.putString(SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY, STUDENT_MAIN)

            //Display Help Bottom Sheet
            helpBottomSheet.show(supportFragmentManager, null)
            helpBottomSheet.arguments = bundle
        }
    }
}