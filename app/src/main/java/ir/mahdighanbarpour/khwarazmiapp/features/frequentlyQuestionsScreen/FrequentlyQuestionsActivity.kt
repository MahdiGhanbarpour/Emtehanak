package ir.mahdighanbarpour.khwarazmiapp.features.frequentlyQuestionsScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityFrequentlyQuestionsBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestions
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestionsArray
import ir.mahdighanbarpour.khwarazmiapp.utils.LOGIN_MAIN
import ir.mahdighanbarpour.khwarazmiapp.utils.LOGIN_OTP
import ir.mahdighanbarpour.khwarazmiapp.utils.SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY
import ir.mahdighanbarpour.khwarazmiapp.utils.asyncRequest
import ir.mahdighanbarpour.khwarazmiapp.utils.isInternetAvailable
import ir.mahdighanbarpour.khwarazmiapp.utils.makeShortToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class FrequentlyQuestionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFrequentlyQuestionsBinding
    private lateinit var frequentlyQuestionsAdapter: FrequentlyQuestionsAdapter

    private val frequentlyQuestionsViewModel: FrequentlyQuestionsViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

    private var sentPage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrequentlyQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        listener()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }


    private fun listener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun initUi() {
        sentPage = intent.getStringExtra(SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY)!!

        frequentlyQuestionsViewModel.insertAllFrequentlyQuestions(
            FrequentlyQuestions(
                LOGIN_MAIN, listOf(
                    FrequentlyQuestionsArray(
                        "چگونه می توانم با استفاده از شماره تلفن خود وارد حساب کاربری شوم ؟",
                        "برای ورود به حساب کاربری، از صفحه لاگین استفاده کنید و شماره تلفن خود را وارد کنید. سپس یک کد تأیید برای شما ارسال می شود که باید آن را در فرم ورود وارد کنید."
                    ),
                    FrequentlyQuestionsArray(
                        "آیا برای لاگین نیاز به رمز عبور دارم ؟",
                        "خیر، برای لاگین نیازی به رمز عبور ندارید. ما از ورود دومرحله ای با ارسال کد تأیید به شماره تلفن استفاده می کنیم."
                    ),
                    FrequentlyQuestionsArray(
                        "آیا می توانم حساب کاربری خود را با چندین شماره تلفن مرتبط کنم ؟",
                        "در حال حاضر، هر حساب کاربری تنها می تواند به یک شماره تلفن مرتبط شود. اگر نیاز به تغییر شماره دارید، می توانید با پشتیبانی تماس بگیرید."
                    ),
                    FrequentlyQuestionsArray(
                        "اگر شماره تلفن خود را فراموش کنم، چطور می توانم به حساب کاربری خود وارد شوم ؟",
                        "اگر شماره تلفن خود را فراموش کردید، با پشتیبانی تماس بگیرید تا به شما کمک کنیم."
                    ),
                    FrequentlyQuestionsArray(
                        "آیا امکان ورود به حساب کاربری با یک دستگاه دیگر وجود دارد ؟",
                        "بله، می توانید از هر دستگاهی به حساب کاربری خود وارد شوید."
                    ),
                )
            )
        )

        frequentlyQuestionsViewModel.insertAllFrequentlyQuestions(
            FrequentlyQuestions(
                LOGIN_OTP, listOf(
                    FrequentlyQuestionsArray(
                        "چگونه می توانم کد فعالسازی را دریافت کنم ؟",
                        "بعد از وارد کردن شماره تلفن، یک پیامک حاوی کد یک بار مصرف به شماره تلفن شما ارسال می شود. این کد را در فیلد قرار داده شده وارد کنید."
                    ),
                    FrequentlyQuestionsArray(
                        "اگر پیامک حاوی کد فعالسازی را دریافت نکردم، چه کار باید بکنم ؟",
                        "در صورتی که پیامکی دریافت نکردید، ابتدا اطمینان حاصل کنید که شماره تلفن وارد شده صحیح است. سپس پس از پایان زمان ارسال دوباره کد، گزینه \"ارسال مجدد کد\" را بررسی کنید. اگر همچنان مشکل پابرجاست، با پشتیبانی تماس بگیرید."
                    ),
                    FrequentlyQuestionsArray(
                        "کد فعالسازی برای چه مدت اعتبار دارد ؟",
                        "کد فعالسازی برای یک مدت زمان کوتاه (60 ثانیه) اعتبار دارد. اگر زمان انقضای کد به پایان رسید، گزینه \"ارسال مجدد کد\" را انتخاب کنید."
                    ),
                )
            )
        )

        if (isInternetAvailable(this)) {
            makeShortToast(this, "گرفتن سوالات متداول از اینترنت")
            loadOfflineData()
            //TODO
        } else {
            loadOfflineData()
        }
    }

    private fun loadOfflineData() {
        if (sentPage.isNullOrEmpty()) {
            sentPageIsNotOk()
        } else {
            frequentlyQuestionsViewModel.getFrequentlyQuestions(sentPage!!).asyncRequest()
                .subscribe(object : SingleObserver<FrequentlyQuestions> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        Snackbar.make(
                            binding.root, "خطا در دریافت اطلاعات", Snackbar.LENGTH_INDEFINITE
                        ).setAction(
                            "تلاش دوباره"
                        ) { loadOfflineData() }.show()
                    }

                    override fun onSuccess(t: FrequentlyQuestions) {
                        initRecycler(t)
                    }
                })
        }
    }

    private fun initRecycler(data: FrequentlyQuestions) {
        frequentlyQuestionsAdapter = FrequentlyQuestionsAdapter(data.frequentlyQuestionsArray)
        binding.recyclerFrequentlyQuestions.adapter = frequentlyQuestionsAdapter

        binding.recyclerFrequentlyQuestions.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun sentPageIsNotOk() {

    }
}