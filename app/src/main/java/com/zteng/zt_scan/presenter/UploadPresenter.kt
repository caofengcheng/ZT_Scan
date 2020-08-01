package com.zteng.zt_scan.presenter

import com.zteng.mvp.base.BasePresenter
import com.zteng.mvp.entity.BitmapInfo
import com.zteng.zt_scan.contract.UploadContract
import com.zteng.zt_scan.model.UploadModel

/**
 *
 * @author caofengcheng on 2020-07-14
 */
class UploadPresenter : BasePresenter<UploadContract.Model, UploadContract.View>(),
    UploadContract.Presenter {
    override lateinit var mView: UploadContract.View

    override fun initModel() {
        mModel = UploadModel()
        mModel!!.setPresenter(this)
    }

    override fun onError(errorMessage: String) {
        mView.onError(errorMessage)
    }

    override fun upload(list: MutableList<BitmapInfo>) {
        mModel?.upload(list)
    }

    override fun isSuccess(success: Boolean, msg: String?) {
        if (!success && msg != null) {
            mView.message(msg)
        }
        mView.isSuccess(success)
    }

}