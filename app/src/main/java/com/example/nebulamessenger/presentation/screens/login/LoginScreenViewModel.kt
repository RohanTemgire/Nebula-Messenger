package com.example.nebulamessenger.presentation.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nebulamessenger.utils.Constants
import com.example.nebulamessenger.credentials.Credentials
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.nebulamessenger.utils.UiLoadingState
import com.example.nebulamessenger.utils.ErrorEvent

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val chatClient: ChatClient
) : ViewModel() {

    private val _errorEvent = MutableSharedFlow<ErrorEvent>()
    val loginErrorEvent = _errorEvent.asSharedFlow()

    var username: String by mutableStateOf("")
        private set

    private val _loadingState = MutableLiveData<UiLoadingState>()
    val loadingState: LiveData<UiLoadingState>
        get() = _loadingState

    fun onEvent(event: LoginScreenEvents) {
        when (event) {
            is LoginScreenEvents.OnUsernameChange -> {
                username = event.username
            }

            is LoginScreenEvents.OnLoginAsUserClicked -> {
                loginUser(username, Credentials.JWT_TOKEN)
            }

            is LoginScreenEvents.OnLoginAsGuestClicked -> {
                loginUser(username)
            }
        }
    }

    private fun loginUser(username: String, token: String? = null) {
        val trimmedUsername = username.trim()
        viewModelScope.launch {
            if (validateUsername(trimmedUsername) && token != null) {
                loginRegisteredUser(username, token)
            } else if (validateUsername(trimmedUsername) && token == null) {
                loginGuestUser(username)
            } else {
                _errorEvent.emit(ErrorEvent.ErrorInputTooShort)
            }
        }
    }

    private fun loginRegisteredUser(username: String, token: String) {
        val user = User(id = username, name = username)

        _loadingState.value = UiLoadingState.IsLoading
        chatClient.getCurrentToken()
        chatClient.connectUser(user, token)
            .enqueue() { result ->
                _loadingState.value = UiLoadingState.IsNotLoading
                if (result.isSuccess) {
                    viewModelScope.launch {
                        _errorEvent.emit(ErrorEvent.Success)
                    }
                } else {
                    viewModelScope.launch {
                        _errorEvent.emit(
                            ErrorEvent.Error(
                                result.error().message ?: "Unknown error occurred"
                            )
                        )
                    }
                }
            }
    }


    private fun loginGuestUser(username: String) {
        _loadingState.value = UiLoadingState.IsLoading
        chatClient.connectGuestUser(userId = username, username = username)
            .enqueue() { result ->
                _loadingState.value = UiLoadingState.IsLoading
                if (result.isSuccess) {
                    viewModelScope.launch {
                        _errorEvent.emit(ErrorEvent.Success)
                    }
                } else {
                    viewModelScope.launch {
                        _errorEvent.emit(
                            ErrorEvent.Error(
                                result.error().message ?: "Unknown error occurred"
                            )
                        )
                    }
                }
            }
    }

    private fun validateUsername(username: String): Boolean {
        return username.length > Constants.MIN_USERNAME_LENGTH
    }

}