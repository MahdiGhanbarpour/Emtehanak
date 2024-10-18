package ir.mahdighanbarpour.emtehanak.model.net

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import ir.mahdighanbarpour.emtehanak.utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Build retrofit and api service
fun apiServiceBuilder(): ApiService {

    // Retrofit builder
    val retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build()

    return retrofit.create(ApiService::class.java)
}