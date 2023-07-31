package com.example.nebulamessenger.utils

sealed class ErrorEvent {
    object ErrorInputTooShort : ErrorEvent()
    data class Error(val error: String) : ErrorEvent()
    object Success : ErrorEvent()
}