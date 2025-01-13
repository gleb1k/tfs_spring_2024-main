package ru.glebik.core.widget.view.recycler

import androidx.recyclerview.widget.DiffUtil

class DiffUtilsCallback<T : DiffListItem>(
    private val oldList: List<T>,
    private val newList: List<T>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].areItemsSame(newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].areContentsSame(newList[newItemPosition])
    }
}