package com.zteng.mvp

import android.util.Log
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.zhy.http.okhttp.OkHttpUtils
import com.zteng.mvp.api.RestApi
import com.zteng.mvp.exception.VoidInterceptor
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *
 * @author caofengcheng on 2020-01-13
 */
object RestManager {
    private var restApi: RestApi? = null

    fun getRestApi(): RestApi? {
        return restApi
    }

    fun initRest(url: String?) {
        val mapper = ObjectMapper()
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(VoidInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .cookieJar(object : CookieJar {
                private var session: List<Cookie>? = null
                override fun saveFromResponse(
                    url: HttpUrl,
                    cookies: List<Cookie>
                ) {
                    session = cookies
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return if (session == null) ArrayList() else session!!
                }
            })
            .addInterceptor { chain ->
                val request = chain.request()
                val builder = request.newBuilder()
                chain.proceed(builder.build())
            }
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            // set your desired log level
            logging.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(logging)
        }
        val client = clientBuilder.build()
        OkHttpUtils.initClient(client)
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(url) //                    .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 使用RxJava作为回调适配器
                .client(client)
                .build()
            restApi = retrofit.create(RestApi::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("TEST", "retrofit初始化失败")
        }
    }

}