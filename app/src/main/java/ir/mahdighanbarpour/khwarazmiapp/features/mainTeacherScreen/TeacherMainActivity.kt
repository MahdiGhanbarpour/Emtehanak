package ir.mahdighanbarpour.khwarazmiapp.features.mainTeacherScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import ir.mahdighanbarpour.khwarazmiapp.R
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityTeacherMainBinding
import ir.mahdighanbarpour.khwarazmiapp.databinding.DialogLogoutBinding
import ir.mahdighanbarpour.khwarazmiapp.features.aboutUsScreen.AboutUsActivity
import ir.mahdighanbarpour.khwarazmiapp.features.classesTeacherScreen.TeacherClassesFragment
import ir.mahdighanbarpour.khwarazmiapp.features.homeTeacherScreen.TeacherHomeFragment
import ir.mahdighanbarpour.khwarazmiapp.features.mainLoginScreen.LoginActivity
import ir.mahdighanbarpour.khwarazmiapp.features.profileTeachersScreen.TeacherProfileFragment
import ir.mahdighanbarpour.khwarazmiapp.features.sharedClasses.HelpBottomSheet
import ir.mahdighanbarpour.khwarazmiapp.features.termsScreen.TermsActivity
import ir.mahdighanbarpour.khwarazmiapp.utils.IS_USER_LOGGED_IN
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.TEACHER
import ir.mahdighanbarpour.khwarazmiapp.utils.TEACHER_MAIN
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_ACTIVITY_YEAR
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_FULL_NAME
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_GRADE
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_PHONE_NUM
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_ROLE
import ir.mahdighanbarpour.khwarazmiapp.utils.USER_STUDY_FIELD
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import ir.mahdighanbarpour.khwarazmiapp.utils.onBackButtonPressed
import org.koin.android.ext.android.inject

class TeacherMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherMainBinding
    private lateinit var editor: Editor

    private val helpBottomSheet = HelpBottomSheet()

    private val firstFragment = TeacherHomeFragment()
    private val secondFragment = TeacherClassesFragment()
    private val thirdFragment = TeacherProfileFragment()

    private val sharedPreferences: SharedPreferences by inject()

    private var active: Fragment = firstFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editor = sharedPreferences.edit()

        onBackButtonPressed {
            customOnBackPressed()
        }

        initBottomNav()
        setDrawerData()
        listener()
    }

    private fun listener() {
        binding.bottomNavTeacherMain.setOnItemReselectedListener { }

        binding.bottomNavTeacherMain.setOnItemSelectedListener {
            // Displaying the relevant page based on the selected item
            when (it.itemId) {
                R.id.homeTeacherFragment -> {
                    supportFragmentManager.beginTransaction().hide(active).show(firstFragment)
                        .commitNow()
                    active = firstFragment

                    Handler(Looper.getMainLooper()).post {
                        firstFragment.onFragmentShowed()
                    }

                    true
                }

                R.id.classesTeacherFragment -> {
                    supportFragmentManager.beginTransaction().hide(active).show(secondFragment)
                        .commitNow()
                    active = secondFragment

                    Handler(Looper.getMainLooper()).post {
                        secondFragment.onFragmentShowed()
                    }

                    true
                }

                R.id.profileTeacherFragment -> {
                    supportFragmentManager.beginTransaction().hide(active).show(thirdFragment)
                        .commitNow()
                    active = thirdFragment

                    Handler(Looper.getMainLooper()).post {
                        thirdFragment.onFragmentShowed()
                    }

                    true
                }

                else -> false
            }
        }

        binding.navViewTeacherMain.setNavigationItemSelectedListener {
            // Performing the relevant work based on the selected item
            when (it.itemId) {
                R.id.menu_home -> {
                    // Closing the navigation drawer and going to the home page
                    binding.drawerLayTeacherMain.closeDrawers()
                    supportFragmentManager.beginTransaction().hide(active).show(firstFragment)
                        .commitNow()
                    active = firstFragment

                    binding.bottomNavTeacherMain.selectedItemId = R.id.homeTeacherFragment
                }

                R.id.menu_classes -> {
                    // Closing the navigation drawer and going to the home classes
                    binding.drawerLayTeacherMain.closeDrawers()
                    supportFragmentManager.beginTransaction().hide(active).show(secondFragment)
                        .commitNow()
                    active = secondFragment

                    binding.bottomNavTeacherMain.selectedItemId = R.id.classesTeacherFragment
                }

                R.id.menu_exams -> {
                    makeShortToast(this, "این بخش در حال توسعه است. با تشکر از شکیبایی شما")
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
                    // Closing the navigation drawer and going to the terms page
                    binding.drawerLayTeacherMain.closeDrawers()
                    val intent = Intent(this, TermsActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_about_us -> {
                    // Closing the navigation drawer and going to the about us page
                    binding.drawerLayTeacherMain.closeDrawers()
                    val intent = Intent(this, AboutUsActivity::class.java)
                    startActivity(intent)
                }

                R.id.menu_support -> {
                    // Closing the navigation drawer and displaying the HelpBottomSheet
                    binding.drawerLayTeacherMain.closeDrawers()
                    showHelpBottomSheet()
                }
            }

            true
        }
    }

    private fun initBottomNav() {
        // Setting bottom navigation items
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment_teacher, thirdFragment, "3").hide(thirdFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment_teacher, secondFragment, "2").hide(secondFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment_teacher, firstFragment, "1").commit()
    }

    @SuppressLint("SetTextI18n")
    private fun setDrawerData() {
        // Placing student information in the drawer
        val navigationView = binding.navViewTeacherMain
        val headerView = navigationView.getHeaderView(0)

        val tvUsername = headerView.findViewById<View>(R.id.tvUsernameDrawerHeader) as TextView
        val tvUserDetail = headerView.findViewById<View>(R.id.tvUserDetailDrawerHeader) as TextView
        val cardViewProfile =
            headerView.findViewById<View>(R.id.cardViewProfileImageDrawerHeader) as MaterialCardView

        cardViewProfile.setCardBackgroundColor(ContextCompat.getColor(this, R.color.teacher_color))
        tvUsername.text = sharedPreferences.getString(USER_FULL_NAME, "خطا")
        tvUserDetail.text = sharedPreferences.getString(USER_STUDY_FIELD, "خطا")
    }

    private fun showLogOutDialog() {
        // Displaying the logout dialog
        val dialog = AlertDialog.Builder(this).create()

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

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun showHelpBottomSheet() {
        // Checking if the Help Bottom Sheet is currently displayed or not
        if (!helpBottomSheet.isVisible) {
            // If it is not showing, it will show it
            val bundle = Bundle()

            bundle.putString(SEND_SELECTED_ROLE_TO_HELP_BOTTOM_SHEET_KEY, TEACHER)
            bundle.putString(SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY, TEACHER_MAIN)

            //Display Help Bottom Sheet
            helpBottomSheet.show(supportFragmentManager, null)
            helpBottomSheet.arguments = bundle
        }
    }

    fun openDrawer() {
        // Opening the navigation drawer
        binding.drawerLayTeacherMain.openDrawer(GravityCompat.START)
    }

    private fun customOnBackPressed(): Boolean {
        // When the user presses the back button, if the drawer was open, it will be closed
        // and if it was closed, the user will be returned to the previous page
        return if (binding.root.isDrawerOpen(GravityCompat.START)) {
            binding.root.closeDrawer(GravityCompat.START)
            true
        } else {
            false
        }
    }
}