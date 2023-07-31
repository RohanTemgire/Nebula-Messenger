package com.example.nebulamessenger.presentation.screens.channelList

sealed class ChannelListEvents{
    object OnHeaderActionClicked : ChannelListEvents()
    object OnDialogDismiss : ChannelListEvents()
    data class OnCreateChannelClicked(val channelName: String) : ChannelListEvents()
    object OnHeaderAvtarClicked : ChannelListEvents()
    object OnLogoutClicked : ChannelListEvents()
}
