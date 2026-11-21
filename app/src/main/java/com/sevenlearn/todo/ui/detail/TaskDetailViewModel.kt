package com.sevenlearn.todo.ui.detail

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenlearn.todo.R
import com.sevenlearn.todo.data.Task
import com.sevenlearn.todo.data.TaskRepository
import com.sevenlearn.todo.navigation.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskDetailUiState(
    val title: String = "",
    val importance: Int = Task.IMPORTANCE_NORMAL,
    val isEditMode: Boolean = false,
    val isLoading: Boolean = true,
)

sealed interface TaskDetailEvent {
    data object Saved : TaskDetailEvent
    data object Deleted : TaskDetailEvent
    data class Error(@param:StringRes val messageRes: Int) : TaskDetailEvent
}

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val taskId: Long? = savedStateHandle
        .get<Long>(NavRoutes.TASK_ID_ARG)
        ?.takeIf { it != NavRoutes.NO_TASK_ID }

    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    private val _events = Channel<TaskDetailEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var existingTask: Task? = null

    init {
        viewModelScope.launch {
            if (taskId != null) {
                val task = repository.getTask(taskId)
                if (task != null) {
                    existingTask = task
                    _uiState.value = TaskDetailUiState(
                        title = task.title,
                        importance = task.importance,
                        isEditMode = true,
                        isLoading = false,
                    )
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(TaskDetailEvent.Error(R.string.error_task_not_found))
                }
            } else {
                _uiState.update { it.copy(isLoading = false, isEditMode = false) }
            }
        }
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onImportanceChange(importance: Int) {
        _uiState.update { it.copy(importance = importance) }
    }

    fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            val title = state.title.trim()
            if (title.isEmpty()) {
                _events.send(TaskDetailEvent.Error(R.string.error_enter_task_title))
                return@launch
            }

            val current = existingTask
            if (current != null) {
                repository.updateTask(
                    current.copy(
                        title = title,
                        importance = state.importance,
                    )
                )
            } else {
                repository.addTask(
                    Task(
                        title = title,
                        importance = state.importance,
                    )
                )
            }
            _events.send(TaskDetailEvent.Saved)
        }
    }

    fun delete() {
        viewModelScope.launch {
            val current = existingTask ?: return@launch
            repository.deleteTask(current)
            _events.send(TaskDetailEvent.Deleted)
        }
    }
}
