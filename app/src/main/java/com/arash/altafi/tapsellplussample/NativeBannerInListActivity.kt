package com.arash.altafi.tapsellplussample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arash.altafi.tapsellplussample.adapter.NativeBannerAdapter
import com.arash.altafi.tapsellplussample.databinding.ActivityNativeBannerInListBinding
import com.arash.altafi.tapsellplussample.enums.ListItemType
import com.arash.altafi.tapsellplussample.model.ItemList
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel

class NativeBannerInListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNativeBannerInListBinding
    private val PAGE_SIZE = 20
    private var currentPage = 0
    private var isLoading = false
    private val items: ArrayList<ItemList> = ArrayList()
    private var adapter: NativeBannerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeBannerInListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        initList()
        getNativeBannerAd()
        generateItems(0)
    }

    private fun initList() {
        binding.apply {
            val layoutManager = LinearLayoutManager(this@NativeBannerInListActivity)
            recyclerView.layoutManager = layoutManager
            adapter = NativeBannerAdapter(this@NativeBannerInListActivity)
            recyclerView.adapter = adapter
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy < 0) {
                        return
                    }
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    if (!isLoading) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                            isLoading = true
                            generateItems(++currentPage)
                        }
                    }
                }
            })
        }
    }

    private fun generateItems(page: Int) {
        for (i in 0 until PAGE_SIZE) {
            val item = ItemList()
            item.title = "item " + (page * PAGE_SIZE + i)
            item.listItemType = ListItemType.ITEM
            items.add(item)
        }
        isLoading = false
        adapter?.updateItem(items)
        getNativeBannerAd()
    }

    private fun getNativeBannerAd() {
        TapsellPlus.requestNativeAd(
            this, BuildConfig.TAPSELL_NATIVE_BANNER, object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    showAd(tapsellPlusAdModel.responseId)
                }

                override fun error(message: String) {
                    Log.d("", "error: ")
                }
            })
    }

    private fun showAd(adIds: String) {
        val item = ItemList()
        item.id = adIds
        item.listItemType = ListItemType.AD
        items.add(item)
        adapter?.updateItem(items)
    }

}