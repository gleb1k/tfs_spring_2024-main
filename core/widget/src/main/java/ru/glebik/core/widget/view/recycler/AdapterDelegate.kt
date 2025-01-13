package ru.glebik.core.widget.view.recycler

import android.view.LayoutInflater
import android.view.ViewGroup

interface AdapterDelegate<T : DiffListItem> {
    fun isForViewType(item: DiffListItem): Boolean
    fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): DelegateViewHolder<T>
}

@Suppress("UNCHECKED_CAST")
fun <T : DiffListItem> AdapterDelegate<*>.castAdapterDelegate(): AdapterDelegate<T> =
    this as AdapterDelegate<T>
