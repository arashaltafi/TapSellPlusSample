package com.arash.altafi.tapsellplussample.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.arash.altafi.tapsellplussample.R
import com.arash.altafi.tapsellplussample.enums.ListItemType
import com.arash.altafi.tapsellplussample.model.ItemList
import com.google.android.material.textview.MaterialTextView
import ir.tapsell.plus.AdHolder
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus

class NativeBannerAdapter(private val activity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(activity)
    private val items: MutableList<ItemList> = ArrayList()

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_AD = 1
    }

    fun updateItem(items: List<ItemList>?) {
        this.items.clear()
        this.items.addAll(items!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_AD) {
            TapsellListItemAdHolder(
                inflater.inflate(R.layout.list_ad_item, parent, false), activity
            )
        } else {
            ItemHolder(inflater.inflate(R.layout.list_item, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].listItemType === ListItemType.ITEM) {
            VIEW_TYPE_ITEM
        } else VIEW_TYPE_AD
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            (holder as ItemHolder).bindView(position)
            return
        }
        (holder as TapsellListItemAdHolder).bindView(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvTitle: MaterialTextView
        fun bindView(position: Int) {
            tvTitle.text = items[position].title
        }

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
        }
    }

    inner class TapsellListItemAdHolder internal constructor(itemView: View, activity: Activity?) :
        RecyclerView.ViewHolder(itemView) {
        private val adContainer: FrameLayout
        private val adHolder: AdHolder?
        fun bindView(position: Int) {
            TapsellPlus.showNativeAd(
                activity,
                items[position].id,
                adHolder,
                object : AdShowListener() {})
        }

        init {
            adContainer = itemView.findViewById(R.id.adContainer)
            adHolder = TapsellPlus.createAdHolder(
                activity, adContainer, R.layout.native_banner
            )
        }
    }

}
