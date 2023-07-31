package com.example.nebulamessenger.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.nebulamessenger.utils.Constants
import com.example.nebulamessenger.utils.ErrorEvent
import com.example.nebulamessenger.presentation.screens.login.LoginScreen
import com.example.nebulamessenger.presentation.screens.login.LoginScreenViewModel
import com.example.nebulamessenger.ui.theme.NebulaMessengerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val viewModel : LoginScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribeToEvents(
            viewModel
        )
        setContent {
            NebulaMessengerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(lifecycleOwner = this, viewModel = viewModel)
                }
            }
        }
    }

    private fun subscribeToEvents(
        viewModel: LoginScreenViewModel,
    ) {
        lifecycleScope.launchWhenCreated {
            viewModel.loginErrorEvent.collect { event ->
                when (event) {
                    is ErrorEvent.ErrorInputTooShort -> {
                        showToast(
                            this@LoginActivity,
                            "Username should be at least ${Constants.MIN_USERNAME_LENGTH} long."
                        )
                    }

                    is ErrorEvent.Error -> {
                        showToast(this@LoginActivity, "Error: ${event.error}")
                    }

                    is ErrorEvent.Success -> {
                        showToast(this@LoginActivity, "Login Successful")
                        Intent(this@LoginActivity,ChannelListActivity::class.java).also {
                            startActivity(it)
                        }
                        finish()
                    }
                }

            }
        }
    }

    private fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}

