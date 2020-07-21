package com.zteng.zt_scan.model

import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.PhoneUtils.call
import com.zteng.common.util.ImageUpload
import com.zteng.mvp.RestManager
import com.zteng.mvp.base.BaseModel
import com.zteng.mvp.base.BaseSingle
import com.zteng.mvp.base.RxComposer
import com.zteng.mvp.base.ServiceResponse
import com.zteng.mvp.entity.BitmapInfo
import com.zteng.zt_scan.contract.UploadContract
import com.zteng.zt_scan.presenter.UploadPresenter
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.functions.Function
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File


/**
 *
 * @author caofengcheng on 2020-07-14
 */
class UploadModel : BaseModel(), UploadContract.Model {
    private var mPresenter: UploadContract.Presenter? = null

    override fun setPresenter(uploadPresenter: UploadPresenter) {
        this.mPresenter = uploadPresenter
    }

    override fun upload(list: MutableList<BitmapInfo>) {
        val mList = ImageUpload.getBuidler(list, "0")
//        val map = mutableMapOf<String, RequestBody>()
//        for (item in list) {
//            val file = File(item.path)
//            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//            map["file\"; filename=\"" + file.name] = requestFile
//        }
        Log.i("UploadModel", mList.toString())

        compositeDisposable?.let {
            RestManager.getRestApi()!!.getFiles(mList)
                .compose(RxComposer.composeSingle())
                .subscribe(object : BaseSingle<ServiceResponse>(it) {
                    override fun onSuccess(result: ServiceResponse) {
                        if (result.code.equals("200")) {
                            Log.i("UploadModel", result.code)
                            mPresenter!!.isSuccess(true)
                        }else{
                            mPresenter!!.isSuccess(false)
                        }
                    }

                    override fun onFailure(e: Throwable, errorMsg: String) {
                        mPresenter!!.onError(errorMsg)
                    }
                })
        }


    }
}