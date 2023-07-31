package com.example.nebulamessenger.presentation.screens.login

sealed class LoginScreenEvents {
    data class OnUsernameChange(val username : String) : LoginScreenEvents()
    object OnLoginAsUserClicked : LoginScreenEvents()
    object OnLoginAsGuestClicked : LoginScreenEvents()
}