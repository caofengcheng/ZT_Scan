package com.zteng.zt_scan.contract

import android.content.Context
import com.zteng.mvp.base.BaseModelInterface
import com.zteng.mvp.base.BasePresenterInterface
import com.zteng.mvp.base.BaseView
import com.zteng.mvp.entity.BitmapInfo
import com.zteng.zt_scan.presenter.UploadPresenter

/**
 *
 * @author caofengcheng on 2020-07-14
 */
interface UploadContract {

    interface Model : BaseModelInterface {
        fun setPresenter(uploadPresenter: UploadPresenter)
        /**
         * 上传图片
         */
        fun upload(list: MutableList<BitmapInfo>)
    }

    interface View : BaseView {
        /**
         * 上传是否成功
         */
        fun isSuccess(success: Boolean)
    }

    interface Presenter : BasePresenterInterface<View> {
        fun upload(list: MutableList<BitmapInfo>)
        fun isSuccess(success: Boolean)
    }

}