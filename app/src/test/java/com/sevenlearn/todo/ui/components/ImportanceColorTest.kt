package com.sevenlearn.todo.ui.components

import com.google.common.truth.Truth.assertThat
import com.sevenlearn.todo.data.Task
import com.sevenlearn.todo.ui.theme.CyanSecondary
import com.sevenlearn.todo.ui.theme.ImportanceNormal
import com.sevenlearn.todo.ui.theme.PurplePrimary
import org.junit.Test

class ImportanceColorTest {
    @Test
    fun mapsImportanceLevels() {
        assertThat(importanceColor(Task.IMPORTANCE_HIGH)).isEqualTo(PurplePrimary)
        assertThat(importanceColor(Task.IMPORTANCE_LOW)).isEqualTo(CyanSecondary)
        assertThat(importanceColor(Task.IMPORTANCE_NORMAL)).isEqualTo(ImportanceNormal)
        assertThat(importanceColor(99)).isEqualTo(ImportanceNormal)
    }
}
