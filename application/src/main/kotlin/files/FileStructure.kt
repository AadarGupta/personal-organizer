package files

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FileItemList(fileList : List<File>) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        for (targetFile in fileList) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                FileListItem(targetFile)
            }
        }
    }
}