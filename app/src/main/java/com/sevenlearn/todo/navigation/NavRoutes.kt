package com.sevenlearn.todo.navigation

object NavRoutes {
    const val TASK_LIST = "task_list"
    const val TASK_ID_ARG = "taskId"
    const val NO_TASK_ID = -1L
    const val TASK_DETAIL = "task_detail?$TASK_ID_ARG={$TASK_ID_ARG}"

    fun taskDetail(taskId: Long = NO_TASK_ID): String =
        "task_detail?$TASK_ID_ARG=$taskId"
}
