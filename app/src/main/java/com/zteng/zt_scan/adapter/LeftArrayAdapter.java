package com.zteng.zt_scan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * LeftArrayAdapter
 * <i>
 * <br>
 * 作者：Created by ly1054 on 2019/3/29.
 * <br>
 * 邮箱：2791014943@qq.com
 * </i>
 */
public class LeftArrayAdapter extends ArrayAdapter<Integer>{

    public LeftArrayAdapter(Context context, Integer[] resIds) {
//        super(context, android.R.layout.simple_list_item_1);
      super(context, android.R.layout.simple_list_item_1, android.R.id.text1, resIds);
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView tv = view.findViewById(android.R.id.text1);
        tv.setTextSize(15);
        tv.setText(getItem(position));
        return view;
    }
}
