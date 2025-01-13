package ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.glebik.core.widget.view.recycler.AdapterDelegate
import ru.glebik.core.widget.view.recycler.DelegateViewHolder
import ru.glebik.core.widget.view.recycler.DiffListItem
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.MessageUiModel
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.ReactionUiModel
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.MessageItem
import ru.glebik.tinkoff_fintech.feature.chat.ui.view.OwnMessageViewGroup

class OwnMessageAdapter(
    private val onLongClick: (Int) -> Unit,
    private val onAddEmojiClick: (Int) -> Unit,
    private val onEmojiClick: (MessageUiModel, ReactionUiModel) -> Unit,
) : AdapterDelegate<MessageItem> {
    override fun isForViewType(item: DiffListItem): Boolean =
        item is MessageItem && item.message.isMeMessage

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): DelegateViewHolder<MessageItem> {
        return OwnMessageViewHolder(
            inflater.inflate(R.layout.item_chat_own_message, parent, false),
            onLongClick,
            onAddEmojiClick,
            onEmojiClick
        )
    }

    class OwnMessageViewHolder(
        view: View,
        private val onShowSheetList: (Int) -> Unit,
        private val onShowSheetReactions: (Int) -> Unit,
        private val onEmojiClick: (MessageUiModel, ReactionUiModel) -> Unit,
    ) : DelegateViewHolder<MessageItem>(view) {

        private val ownMessageViewGroup = itemView.findViewById<OwnMessageViewGroup>(R.id.message)

        override fun bind(item: MessageItem) {
            with(item.message) {
                ownMessageViewGroup.changeComment(content)

                ownMessageViewGroup.addReactions(item.message, onEmojiClick)
            }

            itemView.setOnLongClickListener {
                onShowSheetList(item.message.id)
                true
            }

            ownMessageViewGroup.flex.plus.setOnClickListener {
                onShowSheetReactions(item.message.id)
            }
        }
    }
}