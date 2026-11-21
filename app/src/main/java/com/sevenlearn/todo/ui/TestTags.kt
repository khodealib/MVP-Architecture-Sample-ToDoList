package com.sevenlearn.todo.ui

object TestTags {
    const val SEARCH_FIELD = "search_field"
    const val ADD_TASK_BUTTON = "add_task_button"
    const val DELETE_ALL_BUTTON = "delete_all_button"
    const val TASK_LIST = "task_list"
    const val EMPTY_STATE = "empty_state"
    const val TASK_TITLE_FIELD = "task_title_field"
    const val SAVE_BUTTON = "save_button"
    const val DELETE_TASK_BUTTON = "delete_task_button"
    const val BACK_BUTTON = "back_button"
    const val IMPORTANCE_HIGH = "importance_high"
    const val IMPORTANCE_NORMAL = "importance_normal"
    const val IMPORTANCE_LOW = "importance_low"
    const val LOADING = "loading_indicator"

    fun taskItem(id: Long): String = "task_item_$id"
}
