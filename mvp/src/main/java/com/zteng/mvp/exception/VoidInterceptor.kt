package com.zteng.mvp.exception

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by linlingrong on 2016-08-02.
 */
class VoidInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        // try the request
        Log.d("http","发送请求: ${request.url()}" )
        val response = chain.proceed(request)
        /* 通过如下的办法曲线取到请求完成的数据
         *
         * 原本想通过  originalResponse.body().string()
         * 去取到请求完成的数据,但是一直报错,不知道是okhttp的bug还是操作不当
         *
         * 然后去看了okhttp的源码,找到了这个曲线方法,取到请求完成的数据后
         */
        val responseBody = response.body()
        if (responseBody != null) {
            val source = responseBody.source()
            if (source != null) {
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()
                var charset = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }
                if (charset != null) {
                    val bodyString = buffer.clone().readString(charset)
                    Log.d("http","${request.url()} 返回: $bodyString")
                    Log.d("http","body string length: ${bodyString.length}")
                    if (bodyString.isEmpty() && response.body() != null) {
                        response.body()!!.source().buffer().writeString("null", charset)
                    }
                }
            }
        }
        return response
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
    }
}