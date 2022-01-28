package com.codinginflow.mvvmtodo.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmtodo.data.PreferencesManager
import com.codinginflow.mvvmtodo.data.SortOrder
import com.codinginflow.mvvmtodo.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor(
    val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager
): ViewModel() {
    val searchQuery = MutableStateFlow("")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksflow = combine(
        searchQuery,
        preferencesFlow
    ) { query, filterPrefences ->
        Pair(query, filterPrefences)
    }.flatMapLatest{(query, filterPrefences) ->
        taskDao.getTasks(query, filterPrefences.sortOrder,filterPrefences.hideCompleted)
    }

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedSelected(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    val task = tasksflow.asLiveData()
}
