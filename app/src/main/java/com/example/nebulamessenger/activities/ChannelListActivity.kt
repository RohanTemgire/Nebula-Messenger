package com.example.nebulamessenger.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import com.example.nebulamessenger.R
import com.example.nebulamessenger.presentation.screens.channelList.ChannelListEvents
import com.example.nebulamessenger.presentation.screens.channelList.ChannelListScreenViewModel
import com.example.nebulamessenger.presentation.screens.channelList.components.LogoutDialog
import com.example.nebulamessenger.presentation.screens.channelList.components.ShowChanelDialog
import com.example.nebulamessenger.utils.Constants.CHANNEL_LIST_EXTRA_ID_NAME
import com.example.nebulamessenger.utils.Constants.CHANNEL_LIST_FILTERS
import com.example.nebulamessenger.utils.ErrorEvent
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@AndroidEntryPoint
class ChannelListActivity : ComponentActivity() {
    private val viewModel: ChannelListScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribeToEvents()

        setContent {
            ChatTheme {
                if (viewModel.showCreateChannelDialog) {
                    ShowChanelDialog(viewModel)
                }
                if (viewModel.showLogoutDialog) {
                    LogoutDialog(
                        onLogoutClicked = {
                            viewModel.onEvent(ChannelListEvents.OnLogoutClicked)
                            finish()
                            startActivity(Intent(this,LoginActivity::class.java))
                        },
                        dismiss = { viewModel.onEvent(ChannelListEvents.OnDialogDismiss) }
                    )
                }
                ChannelsScreen(
                    // this filters are used so show only the channels we are specifying here
                    filters = Filters.`in`(
                        fieldName = "type",
                        values = CHANNEL_LIST_FILTERS
                    ),
                    title = stringResource(id = R.string.app_name),
                    isShowingSearch = true,
                    onItemClick = {
                        val intent = Intent(this@ChannelListActivity, MessagesActivity::class.java)
                        intent.putExtra(CHANNEL_LIST_EXTRA_ID_NAME, it.cid)
                        startActivity(intent)
                    },
                    onBackPressed = {
                        finish()
                    },
                    onHeaderActionClick = {
                        viewModel.onEvent(ChannelListEvents.OnHeaderActionClicked)
                    },
                    onHeaderAvatarClick = {
                        viewModel.onEvent(ChannelListEvents.OnHeaderAvtarClicked)
                    }
                )
            }
        }
    }

    private fun subscribeToEvents() {
        lifecycleScope.launchWhenCreated {
            viewModel.createChannelEvent.collect { event ->
                when (event) {
                    is ErrorEvent.Error -> {
                        showToast(event.error)
                    }

                    is ErrorEvent.Success -> {
                        showToast("Channel created!")
                    }

                    else -> {}
                }

            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@ChannelListActivity, message, Toast.LENGTH_SHORT).show()
    }
}
