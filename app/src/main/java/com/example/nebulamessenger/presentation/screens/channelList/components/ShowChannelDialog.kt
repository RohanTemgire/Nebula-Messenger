package com.example.nebulamessenger.presentation.screens.channelList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.nebulamessenger.R
import com.example.nebulamessenger.presentation.screens.channelList.ChannelListEvents
import com.example.nebulamessenger.presentation.screens.channelList.ChannelListScreenViewModel

@Composable
fun ShowChanelDialog(viewModel: ChannelListScreenViewModel) {
    var channelName by remember {
        mutableStateOf("")
    }
    AlertDialog(
        onDismissRequest = {
            viewModel.onEvent(ChannelListEvents.OnDialogDismiss)
        },
        title = {
            Text(
                text = stringResource(id = R.string.create_new_channel),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            OutlinedTextField(
                value = channelName,
                onValueChange = { channelName = it },
                label = {
                    Text(text = "Name")
                },
                placeholder = {
                    Text(text = "e.g. Nebula clan")
                }
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.onEvent(ChannelListEvents.OnCreateChannelClicked(channelName))
                    }
                ) {
                    Text(text = stringResource(id = R.string.create_new_channel))
                }
            }
        }
    )
}