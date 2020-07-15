package com.hazz.kotlinmvp.net

import android.util.Log
import okhttp3.*
import org.slf4j.LoggerFactory
import java.nio.charset.Charset

/**
 * Created by xuhao on 2017/11/16.
 *
 */

class RetrofitManager : Interceptor{
    private val UTF8 = Charset.forName("UTF-8")

    override fun intercept(chain: Interceptor.Chain): Response {
         var request = chain.request()
        Log.d("Http","发送请求: ${request.url()}")
        val response = chain.proceed(request)

        val responseBody = response.body()
        if (responseBody != null){
            val source = responseBody.source()
            if (source != null) {
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()
                var charset: Charset? = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }
                if (charset != null) {
                    val bodyString = buffer.clone().readString(charset)
                    Log.d("","${request.url()} 返回: ${bodyString}")
                    Log.d("","body string length: ${bodyString.length}")
                    if (bodyString.isEmpty() && response.body() != null) {
                        response.body()!!.source().buffer().writeString("null", charset)
                    }
                }
            }
        }
        return response
    }


}
