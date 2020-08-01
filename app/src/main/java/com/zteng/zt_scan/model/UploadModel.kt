package com.zteng.zt_scan.model

import android.util.Log
import com.zteng.common.util.ImageUpload
import com.zteng.mvp.RestManager
import com.zteng.mvp.base.BaseModel
import com.zteng.mvp.base.BaseSingle
import com.zteng.mvp.base.RxComposer
import com.zteng.mvp.base.ServiceResponse
import com.zteng.mvp.entity.BitmapInfo
import com.zteng.zt_scan.contract.UploadContract
import com.zteng.zt_scan.presenter.UploadPresenter


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
        Log.i("UploadModel", mList.toString())
        compositeDisposable?.let {
            RestManager.getRestApi()!!.getFiles(mList)
                .compose(RxComposer.composeSingle())
                .subscribe(object : BaseSingle<ServiceResponse>(it) {
                    override fun onSuccess(result: ServiceResponse) {
                        if (result.code.equals("200")) {
                            Log.i("UploadModel", result.code)
                            mPresenter!!.isSuccess(true,null)
                        }else{
                            mPresenter!!.isSuccess(false,result.message)
                        }
                    }


                    override fun onFailure(e: Throwable, errorMsg: String) {
                        mPresenter!!.onError(errorMsg)
                    }
                })
        }


    }
}