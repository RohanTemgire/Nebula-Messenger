package com.example.nebulamessenger.utils

sealed class UiLoadingState {
    object IsLoading : UiLoadingState()
    object IsNotLoading : UiLoadingState()
}