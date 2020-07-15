package com.zteng.zt_scan.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zteng.common.util.Sp
import com.zteng.zt_scan.R
import kotlinx.android.synthetic.main.fragment_transport.*

class TransportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transport, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sw_item.isChecked = Sp.getBoolean(
            activity,
            "transport_udisk",
            false
        )
        sw_item.setOnCheckedChangeListener { _, isChecked ->
            Sp.setBoolean(activity, "transport_udisk", isChecked)
        }
    }


}
