package com.sevenlearn.todo.navigation

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NavRoutesTest {
    @Test
    fun taskDetail_default_usesNoTaskId() {
        assertThat(NavRoutes.taskDetail()).isEqualTo("task_detail?taskId=-1")
    }

    @Test
    fun taskDetail_withId_buildsRoute() {
        assertThat(NavRoutes.taskDetail(42L)).isEqualTo("task_detail?taskId=42")
    }

    @Test
    fun constants_matchExpectedValues() {
        assertThat(NavRoutes.TASK_LIST).isEqualTo("task_list")
        assertThat(NavRoutes.TASK_ID_ARG).isEqualTo("taskId")
        assertThat(NavRoutes.NO_TASK_ID).isEqualTo(-1L)
        assertThat(NavRoutes.TASK_DETAIL).isEqualTo("task_detail?taskId={taskId}")
    }
}
