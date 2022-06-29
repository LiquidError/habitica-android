package com.habitrpg.wearos.habitica.ui.viewmodels

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.habitrpg.common.habitica.models.responses.TaskDirection
import com.habitrpg.common.habitica.models.tasks.TaskType
import com.habitrpg.wearos.habitica.data.repositories.TaskRepository
import com.habitrpg.wearos.habitica.data.repositories.UserRepository
import com.habitrpg.wearos.habitica.managers.LoadingManager
import com.habitrpg.wearos.habitica.models.tasks.Task
import com.habitrpg.wearos.habitica.util.ExceptionHandlerBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RYAViewModel @Inject constructor(
    userRepository: UserRepository,
    val taskRepository: TaskRepository,
    exceptionBuilder: ExceptionHandlerBuilder, loadingManager: LoadingManager
) : BaseViewModel(userRepository, exceptionBuilder, loadingManager) {
    val tasks = taskRepository.getTasks(TaskType.DAILY)
        .map { it.filter { task -> task.isDue == true && !task.completed } }
        .asLiveData()

    private val tasksToComplete = mutableListOf<Task>()

    fun tappedTask(task: Task) {
        task.completed = !task.completed
        // taskRepository.localRepository.updateTask(task)
        if (task.completed) {
            if (!tasksToComplete.contains(task)) {
                tasksToComplete.add(task)
            }
        } else {
            if (tasksToComplete.contains(task)) {
                tasksToComplete.remove(task)
            }
        }
    }

    fun runCron(function: (Boolean) -> Unit) {
        viewModelScope.launch(exceptionBuilder.userFacing(this)) {
            loadingManager.startLoading()
            for (task in tasksToComplete) {
                taskRepository.scoreTask(null, task, TaskDirection.UP)
            }
            userRepository.runCron()
            userRepository.retrieveUser()
            function(true)
            loadingManager.endLoading()
        }
    }
}
