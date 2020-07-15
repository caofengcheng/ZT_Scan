package com.zteng.zt_scan.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.huagaoscan.sdk.HGScanManager
import com.zteng.mvp.entity.BitmapInfo
import com.zteng.zt_scan.PhotoViewActivity
import com.zteng.zt_scan.R
import io.reactivex.annotations.NonNull
import java.io.File

/**
 *
 * @author caofengcheng on 2020-07-14
 */
class ScanAdapter(private val context: Context,private val mList: MutableList<BitmapInfo>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_preview, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        val holder = viewHolder as ViewHolder
        Log.d(this@ScanAdapter.toString(), "onBindViewHolder: " + mList[i].path)
        if (mList[i].bmp != null) {
            holder.image.setImageBitmap(mList[i].bmp)
        } else {
            val bitmap =
                HGScanManager.getInstance().getCachedPrevImage(mList[i].index)
            if (bitmap != null) {
                holder.image.setImageBitmap(bitmap)
            } else {
                Glide.with(context)
                    .load(Uri.fromFile(File(mList[i].path)))
                    .skipMemoryCache(false)
                    .into(DrawableImageViewTarget(holder.image, true))
            }
        }
        var index = "" + Math.abs(mList[i].index)
        if (HGScanManager.getInstance().setting.isAutoCut) {
            index += if (mList[i].index > 0) "A" else "B"
        } else {
            holder.index.textSize = 8f
        }
        holder.index.text = index
        holder.image.setOnClickListener {
            if (!TextUtils.isEmpty(mList[i].path)) {
                if (File(mList[i].path).exists()) {
                    val intent = Intent(
                        context,
                        PhotoViewActivity::class.java
                    )
                    intent.putExtra("path", mList[i].path)
                    context.startActivity(intent)
                }
            }
        }
    }

    inner class ViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.iv_preview) as ImageView
        var index: TextView = itemView.findViewById(R.id.tv_index) as TextView

    }

}