package ru.glebik.tinkoff_fintech.feature.chat.ui.recycler

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemOffsetDecoration(offsetInDp: Int, context: Context) :
    RecyclerView.ItemDecoration() {

    private val offset: Int = (offsetInDp * context.resources.displayMetrics.density).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        outRect.bottom = offset
    }
}