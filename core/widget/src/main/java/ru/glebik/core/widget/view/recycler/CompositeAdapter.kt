package ru.glebik.core.widget.view.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class CompositeAdapter<T : DiffListItem>(
    private val delegates: List<AdapterDelegate<T>>
) : RecyclerView.Adapter<DelegateViewHolder<T>>() {

    constructor(vararg delegates: AdapterDelegate<T>) : this(delegates.toList())

    private var data: List<T> = emptyList()

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        val delegateIndex = delegates.indexOfFirst { it.isForViewType(item) }
        if (delegateIndex < 0) {
            throw IllegalArgumentException("Unsupported view type, $position $item")
        }
        return delegateIndex
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DelegateViewHolder<T> {
        return delegates[viewType].createViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: DelegateViewHolder<T>, position: Int) =
        holder.bind(data[position])

    fun updateData(newData: List<T>) {
        val callback = DiffUtilsCallback(
            oldList = data,
            newList = newData
        )
        val diffResult = DiffUtil.calculateDiff(callback)
        this.data = newData
        diffResult.dispatchUpdatesTo(this)
    }
}