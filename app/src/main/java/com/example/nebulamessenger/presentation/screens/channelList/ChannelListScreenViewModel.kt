package com.example.nebulamessenger.presentation.screens.channelList

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nebulamessenger.utils.Constants.MIN_CHANNEL_NAME_LENGTH
import com.example.nebulamessenger.utils.ErrorEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChannelListScreenViewModel @Inject constructor(
    private val chatClient: ChatClient
) : ViewModel() {

    private val _createChannelEvent = MutableSharedFlow<ErrorEvent>()
    val createChannelEvent = _createChannelEvent.asSharedFlow()

    var showCreateChannelDialog by mutableStateOf(false)
        private set

    var showLogoutDialog by mutableStateOf(false)
        private set

    fun onEvent(event: ChannelListEvents) {
        when (event) {
            is ChannelListEvents.OnHeaderActionClicked -> {
                showCreateChannelDialog = true
            }

            is ChannelListEvents.OnDialogDismiss -> {
                showCreateChannelDialog = false
                showLogoutDialog = false
            }

            is ChannelListEvents.OnCreateChannelClicked -> {
                createChannel(event.channelName)
            }

            is ChannelListEvents.OnHeaderAvtarClicked -> {
                showLogoutDialog = true
            }

            is ChannelListEvents.OnLogoutClicked -> {
                showLogoutDialog = false
                logout()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun logout() {
        chatClient.disconnect(true)
    }


    private fun createChannel(channelName: String, channelType: String = "messaging") {
        val trimmedChannelName = channelName.trim()
        val channelId = UUID.randomUUID().toString()

        viewModelScope.launch {
            if (trimmedChannelName.isBlank()) {
                _createChannelEvent.emit(ErrorEvent.Error("The channel name can't be blank."))
                return@launch
            }
            if (trimmedChannelName.length < MIN_CHANNEL_NAME_LENGTH) {
                _createChannelEvent.emit(ErrorEvent.Error("Channel name should be at least $MIN_CHANNEL_NAME_LENGTH long."))
                return@launch
            }

            chatClient.createChannel(
                channelType = channelType,
                channelId = channelId,
                memberIds = emptyList(),
                extraData = mapOf(
                    "name" to trimmedChannelName,
                    "image" to "https://www.shutterstock.com/image-vector/chat-bubbles-faces-profiles-cute-600w-1812206755.jpg"
                )
            ).enqueue() { result ->
                showCreateChannelDialog = false
                if (result.isSuccess) {
                    viewModelScope.launch {
                        _createChannelEvent.emit(ErrorEvent.Success)
                    }
                } else {
                    viewModelScope.launch {
                        _createChannelEvent.emit(
                            ErrorEvent.Error(
                                result.error().message ?: "Unknown error occurred"
                            )
                        )
                    }
                }
            }
        }
    }

}