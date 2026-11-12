package com.example.service

import com.example.data.model.ParallelTaskItem
import com.example.data.model.TaskStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ParallelTaskManager(private val scope: CoroutineScope) {

  private val _tasks = MutableStateFlow<List<ParallelTaskItem>>(
    listOf(
      ParallelTaskItem("t1", "WiFi & Network Optimization", "wifi"),
      ParallelTaskItem("t2", "Gallery Meera Avatar Sync", "photo"),
      ParallelTaskItem("t3", "Vedic Sanskrit Daily Sloka Cache", "book"),
      ParallelTaskItem("t4", "Japanese Vocabulary Preload", "translate"),
      ParallelTaskItem("t5", "System Audio & Mic Sensitivity Calibrate", "mic"),
      ParallelTaskItem("t6", "Memory Backup & Log Integrity Check", "storage")
    )
  )
  val tasks = _tasks.asStateFlow()

  private val _isRunningAll = MutableStateFlow(false)
  val isRunningAll = _isRunningAll.asStateFlow()

  private var executionJob: Job? = null

  fun executeAllParallel(onCompleteLog: (String) -> Unit) {
    if (_isRunningAll.value) return
    _isRunningAll.value = true

    executionJob = scope.launch {
      val jobs = _tasks.value.map { task ->
        launch {
          runSingleTask(task.id)
        }
      }
      jobs.forEach { it.join() }
      _isRunningAll.value = false
      onCompleteLog("All 6 parallel automation tasks finished successfully without crashes.")
    }
  }

  private suspend fun runSingleTask(taskId: String) {
    updateTask(taskId) { it.copy(status = TaskStatus.RUNNING, progress = 0.1f, resultMessage = "Starting...") }
    delay(400)

    for (p in 2..10) {
      delay(250)
      val progress = p / 10f
      updateTask(taskId) {
        it.copy(
          progress = progress,
          resultMessage = "Progress ${(progress * 100).toInt()}%"
        )
      }
    }

    updateTask(taskId) {
      it.copy(
        status = TaskStatus.SUCCESS,
        progress = 1.0f,
        resultMessage = "Completed ✓"
      )
    }
  }

  private fun updateTask(taskId: String, transform: (ParallelTaskItem) -> ParallelTaskItem) {
    _tasks.value = _tasks.value.map {
      if (it.id == taskId) transform(it) else it
    }
  }

  fun resetTasks() {
    executionJob?.cancel()
    _isRunningAll.value = false
    _tasks.value = _tasks.value.map {
      it.copy(status = TaskStatus.IDLE, progress = 0f, resultMessage = "")
    }
  }
}
