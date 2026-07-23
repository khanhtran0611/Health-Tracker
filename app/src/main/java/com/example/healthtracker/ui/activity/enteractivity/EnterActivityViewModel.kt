package com.example.healthtracker.ui.activity.enteractivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.Activity
import com.example.healthtracker.domain.repository.ActivityRepository
import com.example.healthtracker.ui.toast.ToastMessage
import com.example.healthtracker.ui.toast.ToastType
import com.example.healthtracker.ui.toast.ToastController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterActivityViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
    private val toastController: ToastController,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EnterActivityUiState())
    val uiState: StateFlow<EnterActivityUiState> = _uiState.asStateFlow()

    private var activityId: Long? = null

    private val _savedEvent = Channel<Unit>(Channel.BUFFERED)
    val savedEvent: Flow<Unit> = _savedEvent.receiveAsFlow()

    fun initialize(activity: Activity?) {
        activityId = activity?.id
        _uiState.value = EnterActivityUiState(
            name = activity?.name ?: "",
            metInput = activity?.met?.let(::formatMet) ?: "",
        )
    }

    fun onNameChange(name: String) {
        _uiState.update { validateEnterActivityForm(it.copy(name = name)) }
    }

    fun onMetChange(met: String) {
        _uiState.update { validateEnterActivityForm(it.copy(metInput = met)) }
    }

    fun onSubmit() {
        val validated = validateEnterActivityForm(_uiState.value)
        _uiState.value = validated
        if (!isEnterActivityFormValid(validated)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val activity = Activity(
                    id = activityId ?: 0,
                    name = validated.name,
                    met = validated.metInput.toDouble(),
                )
                if (activityId == null) {
                    activityRepository.addActivity(activity)
                } else {
                    activityRepository.updateActivity(activity)
                }
                toastController.show(ToastMessage(R.string.msg_activity_saved, ToastType.SUCCESS))
                _uiState.update { it.copy(isSaving = false) }
                _savedEvent.send(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                toastController.show(ToastMessage(R.string.msg_activity_save_failed, ToastType.ERROR))
                _uiState.update { it.copy(isSaving = false) }

            }
        }
    }

    fun onDelete() {
        val id = activityId ?: return
        val state = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }
            try {
                activityRepository.deleteActivity(
                    Activity(
                        id = id,
                        name = state.name,
                        met = state.metInput.toDoubleOrNull() ?: 0.0,
                    ),
                )
                toastController.show(ToastMessage(R.string.msg_activity_deleted, ToastType.SUCCESS))
                _uiState.update { it.copy(isDeleting = false) }
                _savedEvent.send(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                toastController.show(ToastMessage(R.string.msg_activity_delete_failed, ToastType.ERROR))
                _uiState.update { it.copy(isDeleting = false) }
            }
        }
    }
}

private fun formatMet(met: Double): String {
    return if (met == met.toInt().toDouble()) {
        met.toInt().toString()
    } else {
        met.toString()
    }
}
