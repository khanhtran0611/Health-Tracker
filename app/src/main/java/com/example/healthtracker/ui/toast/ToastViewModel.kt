package com.example.healthtracker.ui.toast

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ToastViewModel @Inject constructor(
    toastController: ToastController,
) : ViewModel() {
    val messages: Flow<ToastMessage> = toastController.messages
}
