package ru.glebik.tinkoff_fintech.feature.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import kotlinx.coroutines.launch
import ru.glebik.core.designsystem.theme.FintechTheme
import ru.glebik.core.navigation.toCiceroneScreen
import ru.glebik.core.presentation.BaseFragment
import ru.glebik.core.widget.view.recycler.CompositeAdapter
import ru.glebik.core.widget.view.recycler.castAdapterDelegate
import ru.glebik.tinkoff_fintech.R
import ru.glebik.tinkoff_fintech.databinding.FragmentChatBinding
import ru.glebik.tinkoff_fintech.feature.chat.di.DaggerChatComponent
import ru.glebik.tinkoff_fintech.feature.chat.ui.BottomChatComposeView
import ru.glebik.tinkoff_fintech.feature.chat.ui.CenterChatComposeView
import ru.glebik.tinkoff_fintech.feature.chat.ui.TopChatComposeView
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.ItemOffsetDecoration
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.adapter.DateAdapter
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.adapter.MessageAdapter
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.adapter.OwnMessageAdapter
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.adapter.TopicAdapter
import ru.glebik.tinkoff_fintech.feature.chat.ui.recycler.item.ChatItem
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatStore
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatStoreFactory
import ru.glebik.tinkoff_fintech.feature.chat.ui.vm.ChatViewModel
import ru.glebik.tinkoff_fintech.main.di.appComponent
import javax.inject.Inject

class ChatFragment : BaseFragment() {

    private var binding: FragmentChatBinding? = null

    @Inject
    lateinit var storeFactory: ChatStoreFactory

    private val viewModel: ChatViewModel by viewModels {
        val topicName = requireArguments().getString(CHAT_FRAGMENT_TAG_TOPIC)
        val streamName = requireArguments().getString(CHAT_FRAGMENT_TAG_STREAM)!!

        val store = storeFactory.create(topicName, streamName)

        ChatViewModel.provideFactory(store)
    }

    private val glide: RequestManager by lazy {
        Glide.with(this)
    }

    private val adapter: CompositeAdapter<ChatItem> by lazy {
        CompositeAdapter(
            DateAdapter().castAdapterDelegate(),
            MessageAdapter(
                glide,
                viewModel::showBottomSheetList,
                viewModel::showBottomSheetReactions,
                viewModel::onReactionClick,
            ).castAdapterDelegate(),
            OwnMessageAdapter(
                viewModel::showBottomSheetList,
                viewModel::showBottomSheetReactions,
                viewModel::onReactionClick,
            ).castAdapterDelegate(),
            TopicAdapter(
                ::navigateToChatTopic
            ).castAdapterDelegate()
        )
    }

    private fun navigateToChatTopic(
        topicName: String,
        streamName: String,
    ) {
        router.navigateTo(
            ChatScreen(
                topicName,
                streamName
            ).toCiceroneScreen(
                R.animator.fade_in,
                R.animator.fade_out,
            )
        )
    }

    override fun initDagger() {
        DaggerChatComponent.factory().create(this.appComponent()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false).also {
            val itemDecoration = ItemOffsetDecoration(8, requireContext())

            binding = FragmentChatBinding.bind(it)
            binding?.rvChat?.adapter = adapter
            binding?.rvChat?.addItemDecoration(itemDecoration)

            binding?.rvChat?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

                    val totalItem: Int = linearLayoutManager.itemCount
                    val lastVisibleItem: Int = linearLayoutManager.findLastVisibleItemPosition()

                    if (lastVisibleItem == totalItem - 5) {
                        viewModel.loadMoreMessages()
                    }
                }
            })

            binding?.bottomContainer!!.apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
                setContent {
                    FintechTheme {
                        BottomChatComposeView(viewModel)
                    }
                }
            }

            binding?.appBarContainer!!.apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
                setContent {
                    FintechTheme {
                        TopChatComposeView(viewModel = viewModel, router)
                    }
                }
            }

            binding?.centerContainer?.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    FintechTheme {
                        CenterChatComposeView(viewModel = viewModel)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewModel.state.collect { state ->

                if (state.isLoading || state.errorMessage != null) {
                    binding?.centerContainer?.visibility = View.VISIBLE
                    binding?.rvChat?.visibility = View.GONE
                } else {
                    binding?.centerContainer?.visibility = View.GONE
                    binding?.rvChat?.visibility = View.VISIBLE
                }

                adapter.updateData(state.chatUiList)
            }
        }

        lifecycleScope.launch {
            viewModel.label.collect { label ->
                when (label) {
                    is ChatStore.Label.Toast -> Toast.makeText(
                        context,
                        label.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {

        const val SHIMMER_ITEMS_COUNT = 5

        const val CHAT_FRAGMENT_TAG_TOPIC = "CHAT_FRAGMENT_TAG_TOPIC"
        const val CHAT_FRAGMENT_TAG_STREAM = "CHAT_FRAGMENT_TAG_STREAM"

        fun newInstance(topicName: String?, streamName: String) = ChatFragment().apply {
            arguments = Bundle().also { bundle ->
                bundle.putString(CHAT_FRAGMENT_TAG_TOPIC, topicName)
                bundle.putString(CHAT_FRAGMENT_TAG_STREAM, streamName)
            }
        }
    }

}