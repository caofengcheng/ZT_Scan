package com.zteng.common.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import com.zteng.common.util.Tools
import com.zteng.mvp.base.BasePresenterInterface
import com.zteng.mvp.base.BaseView


import io.reactivex.disposables.CompositeDisposable
import org.slf4j.LoggerFactory

@Suppress("CAST_NEVER_SUCCEEDS")
abstract class BaseFragment<T : BasePresenterInterface<*>> : Fragment(), BaseView {
    var mDisposable = CompositeDisposable()
    var mPresenter: T? = null
    protected abstract val presenter: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onResume() {
        super.onResume()
        setUserVisibleHint(true)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isVisible()) {
            getData()
        }
    }

    protected fun init(view: View) {
        mPresenter = presenter
        mPresenter!!.setView(this as Nothing)
        initView(view)
        mPresenter!!.subscribe()
        initData()
        showData()
    }

    override fun onError(errorMessage: String) {
        Log.d("",errorMessage)
        Tools.displayToast(errorMessage)
    }

    //

    /**
     * 初始化view 包含findViewById等
     *
     * @param view fragment
     */
    protected abstract fun initView(view: View)

    /**
     * 获取数据 调用presenter接口
     */
    protected abstract fun initData()

    /**
     * 展示数据 显示到界面上
     */
    protected abstract fun showData()

    /**
     * 释放数据 释放presenter
     */
    protected abstract fun releaseData()

    protected abstract fun getData()


    override fun onDestroy() {
        releaseData()
        if (mPresenter != null) {
            mPresenter!!.unsubscribe()
        }
        if (!mDisposable.isDisposed) {
            mDisposable.dispose()
        }
        super.onDestroy()
    }

}
