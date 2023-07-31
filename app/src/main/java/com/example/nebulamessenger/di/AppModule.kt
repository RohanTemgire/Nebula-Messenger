package com.example.nebulamessenger.di

import android.content.Context
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import com.example.nebulamessenger.credentials.Credentials.API_KEY
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // this is used to provide offline support, it uses side effects
    @Provides
    fun provideOfflinePluginFactory(@ApplicationContext context: Context) =
        StreamOfflinePluginFactory(
            config = Config(
                backgroundSyncEnabled = true,
                userPresence = true,
                persistenceEnabled = true,
                uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING
            ),
            appContext = context
        )

    @Singleton
    @Provides
    fun provideChatClient(@ApplicationContext context: Context, offlinePluginFactory : StreamOfflinePluginFactory): ChatClient {
        return ChatClient.Builder(API_KEY, context)
            .withPlugin(offlinePluginFactory)
            .logLevel(ChatLogLevel.ALL)
            .build()
    }
}