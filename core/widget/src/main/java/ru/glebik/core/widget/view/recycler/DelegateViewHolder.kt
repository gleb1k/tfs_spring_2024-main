package ru.glebik.core.widget.view.recycler

import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

abstract class DelegateViewHolder<T : DiffListItem>(item: View) : RecyclerView.ViewHolder(item) {
    abstract fun bind(item: T)

    protected fun getString(@StringRes res: Int, vararg args: Any): String {
        return itemView.context.getString(res, *args)
    }

    protected fun getStringArray(@ArrayRes res: Int): Array<String> {
        return itemView.context.resources.getStringArray(res)
    }
}