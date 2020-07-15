package com.zteng.mvp.api

import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import com.zteng.mvp.base.ServiceResponse as ServiceResponse


/**
 * 接口api
 */
interface RestApi {

    /**
     * 多上传图片
     */
    @Multipart
    @POST("http://io.ztengit.com/v8/scan/upload")
    fun getFiles(@Part file: List<MultipartBody.Part>): Single<ServiceResponse>

//    /**
//     * 多上传图片
//     */
//    @Multipart
//    @POST("http://io.ztengit.com/v8/scan/upload")
//    fun getFiles(@PartMap maps: MutableMap<String, RequestBody>): Single<ServiceResponse>

}