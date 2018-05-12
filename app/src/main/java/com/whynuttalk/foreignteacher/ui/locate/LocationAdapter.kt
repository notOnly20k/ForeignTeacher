package com.whynuttalk.foreignteacher.ui.locate

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.amap.api.services.core.PoiItem
import com.whynuttalk.foreignteacher.R

/**
 * Created by cz on 4/26/18.
 */
class LocationAdapter(val context: Context) : RecyclerView.Adapter<LocationAdapter.LocateViewHolder>() {
    private var dataList = mutableListOf<PoiItem>()
    private var locationClickedListener: LocationClickedListener? = null
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: LocateViewHolder, position: Int) {
        val poiItem = dataList[position]
        holder.tvTitle.text = poiItem.title
        holder.tvLabel.text = poiItem.cityName + poiItem.adName + poiItem.snippet
        holder.itemView.setOnClickListener {
            locationClickedListener?.onLocationClickedListener(dataList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LocateViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_locate, parent, false)
        return LocateViewHolder(view)
    }

    fun upDataList(list: List<PoiItem>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun setLocationClickedListener(locationClickedListener: LocationClickedListener) {
        this.locationClickedListener = locationClickedListener
    }

    interface LocationClickedListener {
        fun onLocationClickedListener(poiItem: PoiItem)
    }

    class LocateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_title)
        lateinit var tvTitle: TextView
        @BindView(R.id.tv_label)
        lateinit var tvLabel: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }

}
