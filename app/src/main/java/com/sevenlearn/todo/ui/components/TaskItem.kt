package com.sevenlearn.todo.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sevenlearn.todo.data.Task
import com.sevenlearn.todo.ui.theme.CyanSecondary
import com.sevenlearn.todo.ui.theme.ImportanceNormal
import com.sevenlearn.todo.ui.theme.LightGray
import com.sevenlearn.todo.ui.theme.PurplePrimary
import com.sevenlearn.todo.ui.theme.White

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(if (task.isCompleted) PurplePrimary else LightGray)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            if (task.isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        Text(
            text = task.title,
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = if (task.isCompleted) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                },
                color = if (task.isCompleted) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        )

        Box(
            modifier = Modifier
                .width(8.dp)
                .fillMaxHeight()
                .background(
                    color = importanceColor(task.importance),
                    shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                ),
        )
    }
}

fun importanceColor(importance: Int): Color = when (importance) {
    Task.IMPORTANCE_HIGH -> PurplePrimary
    Task.IMPORTANCE_LOW -> CyanSecondary
    else -> ImportanceNormal
}
