package com.example.nebulamessenger.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.nebulamessenger.utils.Constants.CHANNEL_LIST_EXTRA_ID_NAME
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes

class MessagesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelId = intent.getStringExtra(CHANNEL_LIST_EXTRA_ID_NAME)

        if (channelId == null) {
            finish()
            return
        }
        setContent {
            ChatTheme(
                shapes = StreamShapes.defaultShapes().copy(
                    avatar = RoundedCornerShape(8.dp),
                    attachment = RoundedCornerShape(16.dp),
                    myMessageBubble = RoundedCornerShape(16.dp),
                    otherMessageBubble = RoundedCornerShape(16.dp),
                    inputField = RectangleShape
                )
            ) {
                MessagesScreen(
                    channelId = channelId,
                    messageLimit = 30,
                    onBackPressed = {
                        finish()
                    }
                )
            }
        }
    }
}