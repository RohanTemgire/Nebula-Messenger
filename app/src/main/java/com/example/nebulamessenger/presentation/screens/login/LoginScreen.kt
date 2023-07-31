package com.example.nebulamessenger.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.nebulamessenger.R
import com.example.nebulamessenger.utils.Dimensions.CHAT_LOGO_SIZE
import com.example.nebulamessenger.utils.Dimensions.LARGE_PADDING
import com.example.nebulamessenger.utils.UiLoadingState

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel,
    lifecycleOwner : LifecycleOwner
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(LARGE_PADDING)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var showProgress: Boolean by remember {
            mutableStateOf(false)
        }

        viewModel.loadingState.observe(lifecycleOwner, Observer { event ->
            showProgress = when(event){
                is UiLoadingState.IsLoading -> {
                    true
                }

                is UiLoadingState.IsNotLoading -> {
                    false
                }
            }

        })

        Image(
            painter = painterResource(id = R.drawable.chat_logo),
            contentDescription = stringResource(id = R.string.chat_logo),
            modifier = Modifier.size(CHAT_LOGO_SIZE)
        )

        Spacer(modifier = Modifier.padding(LARGE_PADDING))

        OutlinedTextField(
            value = viewModel.username,
            onValueChange = { viewModel.onEvent(LoginScreenEvents.OnUsernameChange(it)) },
            label = {
                Text(text = stringResource(id = R.string.username))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.enter_username))
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            maxLines = 1
        )

        Spacer(modifier = Modifier.padding(LARGE_PADDING))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.onEvent(LoginScreenEvents.OnLoginAsUserClicked) }
        ) {
            Text(text = stringResource(id = R.string.login_as_user))
        }

        Spacer(modifier = Modifier.padding(LARGE_PADDING))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.onEvent(LoginScreenEvents.OnLoginAsGuestClicked) }
        ) {
            Text(text = stringResource(id = R.string.login_as_guest))
        }

        Spacer(modifier = Modifier.padding(LARGE_PADDING))

        if (showProgress) {
            CircularProgressIndicator()
        }
    }
}



