package com.codinginflow.mvvmtodo.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.codinginflow.mvvmtodo.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class TasksViewModel @ViewModelInject constructor(
    val taskDao: TaskDao
): ViewModel() {
    val searchQuery = MutableStateFlow("")

    private val tasksflow = searchQuery.flatMapLatest {
        taskDao.getTasks(it)
    }

    val task = tasksflow.asLiveData()
}