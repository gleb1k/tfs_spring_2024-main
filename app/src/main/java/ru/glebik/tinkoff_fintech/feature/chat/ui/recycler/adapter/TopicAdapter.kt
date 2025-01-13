package ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.glebik.core.widget.view.recycler.AdapterDelegate
import ru.glebik.core.widget.view.recycler.DelegateViewHolder
import ru.glebik.core.widget.view.recycler.DiffListItem
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.TopicItem

class TopicAdapter(
    private val onClick: (String, String) -> Unit,
) : AdapterDelegate<TopicItem> {

    override fun isForViewType(item: DiffListItem): Boolean = item is TopicItem
    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): DelegateViewHolder<TopicItem> {
        return TopicViewHolder(
            inflater.inflate(R.layout.item_chat_topic, parent, false),
            onClick = onClick
        )
    }

    class TopicViewHolder(
        view: View,
        private val onClick: (String, String) -> Unit,
    ) : DelegateViewHolder<TopicItem>(view) {

        private val nameText = itemView.findViewById<TextView>(R.id.tv_topic)

        override fun bind(item: TopicItem) {
            nameText.text = "#${item.topic.topicName}"

            itemView.setOnClickListener {
                onClick(item.topic.topicName, item.topic.streamName)
            }
        }
    }
}