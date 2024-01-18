package ir.mahdighanbarpour.khwarazmiapp.features.mainStudentScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityStudentMainBinding
import ir.mahdighanbarpour.khwarazmiapp.databinding.DialogLogoutBinding
import ir.mahdighanbarpour.khwarazmiapp.features.aboutUsScreen.AboutUsActivity
import ir.mahdighanbarpour.khwarazmiapp.features.homeStudentScreen.StudentHomeFragment
import ir.mahdighanbarpour.khwarazmiapp.features.mainLoginScreen.LoginActivity
import ir.mahdighanbarpour.khwarazmiapp.features.profileStudentScreen.StudentProfileFragment
import ir.mahdighanbarpour.khwarazmiapp.features.teachersStudentScreen.StudentTeachersFragment
import ir.mahdighanbarpour.khwarazmiapp.features.termsScreen.TermsActivity
import ir.mahdighanbarpour.khwarazmiapp.utils.HelpBottomSheet
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT
import ir.mahdighanbarpour.khwarazmiapp.utils.STUDENT_MAIN
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast

class StudentMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentMainBinding

    private val helpBottomSheet = HelpBottomSheet()

    private val firstFragment = StudentHomeFragment()
    private val secondFragment = StudentTeachersFragment()
    private val thirdFragment = StudentProfileFragment()

    private var active: Fragment = firstFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNav()
        listener()
    }

    private fun listener() {
        binding.bottomNavStudentMain.setOnItemReselectedListener { }

        binding.bottomNavStudentMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeStudentFragment -> {
                    supportFragmentManager.beginTransaction().hide(active).show(firstFragment)
                        .commitNow()
                    active = firstFragment

//                    Handler(Looper.getMainLooper()).post {
//                        firstFragment.onFragmentShowed()
//                    }

                    true
                }

                R.id.teachersStudentFragment -> {
                    supportFragmentManager.beginTransaction().hide(active).show(secondFragment)
                        .commitNow()
                    active = secondFragment

//                    Handler(Looper.getMainLooper()).post {
//                        secondFragment.onFragmentShowed()
//                    }

                    true
                }

                R.id.profileStudentFragment -> {
                    supportFragmentManager.beginTransaction().hide(active).show(thirdFragment)
                        .commitNow()
                    active = thirdFragment

//                    Handler(Looper.getMainLooper()).post {
//                        thirdFragment.onFragmentShowed()
//                    }

                    true
                }

                else -> false
            }
        }

        binding.navViewStudentMain.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    // TODO
                }

                R.id.menu_exams -> {
                    makeShortToast(this, "بخش آزمون ها در حال توسعه است. با تشکر از شکیبایی شما")
                    // TODO
                }

                R.id.menu_teachers -> {
                    // TODO
                }

                R.id.menu_setting -> {
                    makeShortToast(this, "تنظیمات در حال توسعه است. با تشکر از شکیبایی شما")
                    // TODO
                }

                R.id.menu_logout -> {
                    showLogOutDialog()
                }

                R.id.menu_terms -> {
                    val intent = Intent(this, TermsActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_about_us -> {
                    val intent = Intent(this, AboutUsActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_support -> {
                    binding.drawerLayStudentMain.closeDrawers()
                    showHelpBottomSheet()
                }
            }

            true
        }
    }

    private fun initBottomNav() {
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment_student, thirdFragment, "3").hide(thirdFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment_student, secondFragment, "2").hide(secondFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment_student, firstFragment, "1").commit()
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