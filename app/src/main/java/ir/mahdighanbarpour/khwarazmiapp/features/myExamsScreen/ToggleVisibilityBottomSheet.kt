package ir.mahdighanbarpour.khwarazmiapp.features.myExamsScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.databinding.BottomSheetToggleVisibilityBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.Exam
import ir.mahdighanbarpour.khwarazmiapp.model.data.ExamsMainResult
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_SELECTED_EXAM_TO_TOGGLE_VISIBILITY_BOTTOM_SHEET_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.getParcelableCustom
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class ToggleVisibilityBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetToggleVisibilityBinding
    private lateinit var exam: Exam

    private val myExamsViewModel: MyExamsViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

    private var selectedVisibility = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetToggleVisibilityBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exam = requireArguments().getParcelableCustom(
            SEND_SELECTED_EXAM_TO_TOGGLE_VISIBILITY_BOTTOM_SHEET_KEY
        )

        setData()
        listener()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Clear the values in Composite Disposable
        compositeDisposable.clear()
    }

    private fun listener() {
        binding.btToggleVisibility.setOnClickListener {
            if (binding.animationViewToggleVisibility.visibility == View.GONE) {
                checkRadioButtons()
            }
        }
        binding.rbPublicToggleVisibility.setOnClickListener {
            selectedVisibility = "1"
        }
        binding.rbHideToggleVisibility.setOnClickListener {
            selectedVisibility = "0"
        }
    }

    private fun setData() {
        when (exam.visibility) {
            "1" -> binding.rbPublicToggleVisibility.isChecked = true
            "0" -> binding.rbHideToggleVisibility.isChecked = true
            else -> binding.rbExclusiveToggleVisibility.isChecked = true
        }

        binding.tvTitleToggleVisibility.text = "تغییر نمایانی ${exam.name}"
    }

    private fun checkRadioButtons() {
        if ((selectedVisibility == "1" && exam.visibility == "1") || (selectedVisibility == "0" && exam.visibility == "0")) {
            dismiss()
        } else {
            // Checking if the user has internet or not
            if (isInternetAvailable(requireContext())) {
                playLoadingAnim()
                changeVisibility(selectedVisibility)
            } else {
                // Display the error of not connecting to the Internet
                Snackbar.make(binding.root, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE)
                    .setAction("تلاش مجدد") { checkRadioButtons() }.show()
            }
        }
    }

    private fun changeVisibility(visibility: String, retries: Int = 5) {
        myExamsViewModel.changeVisibility(exam.id, visibility).asyncRequest()
            .subscribe(object : SingleObserver<ExamsMainResult> {
                override fun onSubscribe(d: Disposable) {
                    // Add the disposable to Composite Disposable
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    if (retries > 0) {
                        // Retry
                        playLoadingAnim()
                        changeVisibility(visibility, retries - 1)
                    } else {
                        // Error report to user
                        Snackbar.make(
                            binding.root, "خطا در دریافت اطلاعات", Snackbar.LENGTH_LONG
                        ).setAction(
                            "تلاش دوباره"
                        ) { checkRadioButtons() }.show()
                    }
                }

                override fun onSuccess(t: ExamsMainResult) {
                    if (t.status == 200) {
                        makeShortToast(requireContext(), "نمایانی با موفقیت تغییر کرد")
                        (requireActivity() as MyExamsActivity).initData()
                        dismiss()
                    } else {
                        // Error report to user
                        Snackbar.make(
                            binding.root, "خطا در تغییر نمایانی", Snackbar.LENGTH_LONG
                        ).setAction(
                            "تلاش دوباره"
                        ) { checkRadioButtons() }.show()
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun playLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(myExamsViewModel.isDataLoading.subscribe {
            requireActivity().runOnUiThread {
                if (it) {
                    binding.rgToggleVisibility.isClickable = false

                    binding.animationViewToggleVisibility.visibility = View.VISIBLE
                    binding.btToggleVisibility.text = null
                    binding.animationViewToggleVisibility.playAnimation()
                } else {
                    binding.animationViewToggleVisibility.visibility = View.GONE
                    binding.btToggleVisibility.text = "تغییر نمایانی"
                    binding.animationViewToggleVisibility.pauseAnimation()

                    binding.rgToggleVisibility.isClickable = true
                }
            }
        })
    }
}