package com.example.healthtracker.ui.toast

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToastController @Inject constructor() {
    private val _messages = Channel<ToastMessage>(Channel.BUFFERED)
    val messages: Flow<ToastMessage> = _messages.receiveAsFlow()

    suspend fun show(message: ToastMessage) {
        _messages.send(message)
    }
}
