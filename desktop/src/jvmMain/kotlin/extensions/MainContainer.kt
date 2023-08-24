package extensions

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RowVA(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable RowScope.() -> Unit
) = Row(
    modifier,
    horizontalArrangement,
    Alignment.CenterVertically,
    content
)

@Composable
fun ColumnVA(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = verticalArrangement,
    content = content
)
