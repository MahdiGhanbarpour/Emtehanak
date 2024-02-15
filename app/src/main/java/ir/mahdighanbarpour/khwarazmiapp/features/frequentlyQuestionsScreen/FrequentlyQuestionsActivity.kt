package ir.mahdighanbarpour.khwarazmiapp.features.frequentlyQuestionsScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ir.mahdighanbarpour.khwarazmiapp.databinding.ActivityFrequentlyQuestionsBinding
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestion
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestionsMainResult
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

        // If the page is closed, it empties the compositeDisposable, which holds values of the Disposable type
        compositeDisposable.clear()
    }


    private fun listener() {
        binding.ivBack.setOnClickListener {
            // If the back button is pressed, the page will be closed
            finish()
        }
    }

    private fun initUi() {
        // Getting the submitted page name
        sentPage = intent.getStringExtra(SEND_PAGE_NAME_TO_FREQUENTLY_QUESTIONS_PAGE_KEY)!!

        // Checking if the user has internet or not
        if (isInternetAvailable(this)) {
            // Getting data from the server
            playLoadingAnim()
            loadOnlineData()
        } else {
            // Getting data from the local database
            loadOfflineData()
        }
    }

    private fun loadOnlineData() {
        // Getting a list of frequently asked questions from the server
        frequentlyQuestionsViewModel.getFrequentlyQuestionsOnline(sentPage!!).asyncRequest()
            .subscribe(object : SingleObserver<FrequentlyQuestionsMainResult> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    // Error report to user
                    Snackbar.make(
                        binding.root,
                        "خطا در دریافت اطلاعات از سرور! سوالات ذخیره شده نمایش داده میشود.",
                        Snackbar.LENGTH_LONG
                    ).setAction(
                        "تلاش دوباره"
                    ) { initUi() }.show()

                    loadOfflineData()
                }

                override fun onSuccess(t: FrequentlyQuestionsMainResult) {
                    // Checking if FAQs have been found for the submitted page
                    if (t.status == 200) {
                        // Starting RecyclerView with sent data
                        initRecycler(t.result.frequentlyQuestions)
                        frequentlyQuestionsViewModel.insertAllFrequentlyQuestions(t.result.frequentlyQuestions)
                    } else if (t.status == 404) {
                        makeShortToast(
                            this@FrequentlyQuestionsActivity,
                            "سوالات متداولی برای این صفحه یافت نشدند!"
                        )
                        finish()
                    }
                }
            })
    }

    private fun loadOfflineData() {
        // Checking that the name of the page is correct and not null
        if (sentPage.isNullOrEmpty()) {
            // The name of the page is broken
            sentPageIsNotOk()
        } else {
            // Getting data from the local database with the help of RxJava
            frequentlyQuestionsViewModel.getFrequentlyQuestionsLocal(sentPage!!).asyncRequest()
                .subscribe(object : SingleObserver<List<FrequentlyQuestion>> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        // Error report to user
                        Snackbar.make(
                            binding.root, "خطا در دریافت اطلاعات", Snackbar.LENGTH_INDEFINITE
                        ).setAction(
                            "تلاش دوباره"
                        ) { initUi() }.show()
                    }

                    override fun onSuccess(t: List<FrequentlyQuestion>) {
                        // Checking if FAQs have been found for the submitted page
                        if (t.isEmpty()) {
                            makeShortToast(
                                this@FrequentlyQuestionsActivity,
                                "سوالات متداولی برای این صفحه یافت نشدند!"
                            )
                            finish()
                        } else {
                            // Starting RecyclerView with sent data
                            initRecycler(t)
                        }
                    }
                })
        }
    }

    private fun initRecycler(data: List<FrequentlyQuestion>) {
        // Making the adapter and making the necessary settings
        frequentlyQuestionsAdapter = FrequentlyQuestionsAdapter(data)
        binding.recyclerFrequentlyQuestions.adapter = frequentlyQuestionsAdapter

        binding.recyclerFrequentlyQuestions.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun playLoadingAnim() {
        // If the information is being received from the server, an animation will be played
        compositeDisposable.add(frequentlyQuestionsViewModel.isDataLoading.subscribe {
            runOnUiThread {
                if (it) {
                    binding.animationViewFrequentlyQuestions.visibility = View.VISIBLE
                    binding.animationViewFrequentlyQuestions.playAnimation()
                } else {
                    binding.animationViewFrequentlyQuestions.visibility = View.GONE
                    binding.animationViewFrequentlyQuestions.pauseAnimation()
                }
            }
        })
    }

    private fun sentPageIsNotOk() {
        // The name of the page is broken
        // TODO
    }
}