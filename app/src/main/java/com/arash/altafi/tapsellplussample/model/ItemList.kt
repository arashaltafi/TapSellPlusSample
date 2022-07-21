package com.arash.altafi.tapsellplussample.model

import com.arash.altafi.tapsellplussample.enums.ListItemType
import java.io.Serializable

class ItemList : Serializable {
    var listItemType: ListItemType? = null
    var id: String? = null
    var title: String? = null
}