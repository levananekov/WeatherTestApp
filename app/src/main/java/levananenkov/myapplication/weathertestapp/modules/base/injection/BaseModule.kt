package levananenkov.myapplication.weathertestapp.modules.base.injection

import android.content.Context

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


abstract class BaseModule<Api>(open var context: Context) {

    companion object {
        val BASE_POI_URL = "https://samples.openweathermap.org/data/2.5/"
    }

    protected val baseUrl: String
        get() = BASE_POI_URL

    protected val baseBuilder: Retrofit.Builder
        get() {
            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val originalHttpUrl = original.url()

                    val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("appid", "1111111111")
                        .build()

                    // Request customization: add request headers
                    val requestBuilder = original.newBuilder()
                        .url(url)

                    val request = requestBuilder.build()
                    return chain.proceed(request)
                }
            })

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)

            val client = httpClient.build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        }

    fun provideApi(context: Context, apiClassName: Class<Api>): Api {
        checkExistsContext()

        this.context = context

        val retrofit = provideApiRetrofit()

        return retrofit.create(apiClassName)
    }

    protected fun provideApiRetrofit(): Retrofit {
        checkExistsContext()

        val httpClient = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)

        val client = httpClient.build()

        return Retrofit.Builder()
            .baseUrl(BASE_POI_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }


    protected fun checkExistsContext() {
        if (context == null) {
            throw RuntimeException("context == null")
        }
    }
}
