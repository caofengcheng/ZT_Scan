package com.zteng.zt_scan

import android.os.Bundle
import android.support.v4.app.Fragment

import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import com.zteng.zt_scan.adapter.LeftArrayAdapter
import com.zteng.zt_scan.fragment.AboutFragment
import com.zteng.zt_scan.fragment.TransportFragment
import com.zteng.zt_scan.fragment.WifiFragment
import kotlinx.android.synthetic.main.activity_system_setting.*

class SystemSettingActivity : AppCompatActivity() {

    var titles =
        arrayOf(R.string.about, R.string.transport_setting, R.string.wifi)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_setting)
        showFragment(AboutFragment())
        init()
    }

    //初始化
    private fun init() {
        val arrayAdapter: ArrayAdapter<Int> = LeftArrayAdapter(this, titles)
        //ListView视图加载适配器
        lv_left.setAdapter(arrayAdapter)
        //为列表视图中选中的项添加响应事件
        lv_left.setOnItemClickListener { parent, _, pos, _ ->
            when (val itemId = parent.getItemAtPosition(pos) as Int) { //获取选择项的值
                R.string.about -> {
                    tv_right_title.setText(itemId)
                    showFragment(AboutFragment())
                }
                R.string.wifi -> {
                    tv_right_title.setText(itemId)
                    showFragment(WifiFragment())
                }
                R.string.transport_setting -> {
                    tv_right_title.setText(itemId)
                    showFragment(TransportFragment())
                }
            }
        }
    }

    /**
     * 返回
     */
    fun backTv(view: View) {
        finish()
    }

    private fun showFragment(fragment: Fragment?) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        if (fragment == null) {
            ll_container.removeAllViews()
        } else {
            transaction.replace(R.id.ll_container, fragment)
        }
        transaction.commit()
    }
}
