package com.zteng.common.util

import android.util.Log
import com.zteng.mvp.entity.BitmapInfo
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object ImageUpload {
    fun getBuidler(
        pictureFile: List<BitmapInfo>,
        type: String?
    ): List<MultipartBody.Part> {
        val builder = MultipartBody.Builder()
        for (filePath in pictureFile) {
            val file = File(filePath.path)
            val body = RequestBody.create(MediaType.parse("image/*"), file)
            Log.i("ImageUpload","fileName:"+file.name+"     filePath:"+file.path)
            builder.addFormDataPart("files", file.name, body) //添加图片数据，body创建的请求体
        }
        builder.addFormDataPart("businessType", type)
        builder.setType(MultipartBody.FORM)
        return builder.build().parts()
    }
}