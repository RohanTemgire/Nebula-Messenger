package com.example.nebulamessenger.presentation.screens.channelList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.nebulamessenger.R
import com.example.nebulamessenger.utils.Dimensions.MEDIUM_PADDING

@Composable
fun LogoutDialog(onLogoutClicked: () -> Unit, dismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = dismiss,
        title = {
            Text(
                text = "Logout?",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(text = stringResource(id = R.string.logout_dialog_description))
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(0.5f),
                    onClick = dismiss
                ) {
                    Text(text = "Cancel")
                }

                Spacer(modifier = Modifier.width(MEDIUM_PADDING))

                Button(
                    modifier = Modifier.weight(0.5f),
                    onClick = onLogoutClicked
                ) {
                    Text(text = "Logout")
                }
            }
        }
    )
}
