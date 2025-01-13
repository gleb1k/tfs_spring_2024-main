package ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import ru.glebik.core.widget.view.recycler.AdapterDelegate
import ru.glebik.core.widget.view.recycler.DelegateViewHolder
import ru.glebik.core.widget.view.recycler.DiffListItem
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.MessageUiModel
import ru.glebik.tinkoff_fintech.feature.chat.ui.model.ReactionUiModel
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.MessageItem
import ru.glebik.tinkoff_fintech.feature.chat.ui.view.MessageViewGroup


class MessageAdapter(
    private val glide: RequestManager,
    private val onLongClick: (Int) -> Unit,
    private val onAddEmojiClick: (Int) -> Unit,
    private val onEmojiClick: (MessageUiModel, ReactionUiModel) -> Unit,
) : AdapterDelegate<MessageItem> {
    override fun isForViewType(item: DiffListItem): Boolean =
        item is MessageItem && !item.message.isMeMessage

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): DelegateViewHolder<MessageItem> {
        return MessageViewHolder(
            inflater.inflate(R.layout.item_chat_message, parent, false),
            glide,
            onLongClick,
            onAddEmojiClick,
            onEmojiClick
        )
    }

    class MessageViewHolder(
        view: View,
        private val glide: RequestManager,
        private val onShowSheetList: (Int) -> Unit,
        private val onShowSheetReactions: (Int) -> Unit,
        private val onEmojiClick: (MessageUiModel, ReactionUiModel) -> Unit,
    ) : DelegateViewHolder<MessageItem>(view) {

        private val messageViewGroup = itemView.findViewById<MessageViewGroup>(R.id.message)

        override fun bind(item: MessageItem) {

            with(item.message) {
                glide
                    .load(avatarUrl)
                    .circleCrop()
                    .into(messageViewGroup.avatar)


                messageViewGroup.changeComment(content)
                messageViewGroup.changeName(senderFullName.orEmpty())

                messageViewGroup.addReactions(item.message, onEmojiClick)
            }

            itemView.setOnLongClickListener {
                onShowSheetList(item.message.id)
                true
            }

            messageViewGroup.flex.plus.setOnClickListener {
                onShowSheetReactions(item.message.id)
            }
        }
    }
}