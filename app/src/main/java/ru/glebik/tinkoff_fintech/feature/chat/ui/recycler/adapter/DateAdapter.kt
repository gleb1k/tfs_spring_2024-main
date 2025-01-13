package ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.glebik.core.widget.view.recycler.AdapterDelegate
import ru.glebik.core.widget.view.recycler.DelegateViewHolder
import ru.glebik.core.widget.view.recycler.DiffListItem
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.DateItem

class DateAdapter : AdapterDelegate<DateItem> {
    override fun isForViewType(item: DiffListItem): Boolean = item is DateItem
    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): DelegateViewHolder<DateItem> {
        return DateViewHolder(
            inflater.inflate(R.layout.item_chat_date, parent, false),
        )
    }

    class DateViewHolder(
        view: View,
    ) : DelegateViewHolder<DateItem>(view) {

        private val dateText = itemView.findViewById<TextView>(R.id.tv_date)

        override fun bind(item: DateItem) {
            with(item.date) {
                val month = if (monthNum in 1..12) {
                    getStringArray(R.array.chat_date_month_array)[monthNum - 1]
                } else {
                    "null"
                }
                dateText.text = getString(R.string.chat_date, day, month)
            }
        }

    }
}