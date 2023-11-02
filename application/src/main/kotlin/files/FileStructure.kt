package files

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

@Composable
fun FileItemList(fileLevel: MutableState<String>) {

    var fileList = mutableListOf<File>();

    transaction {
        for (file in FileDataObject.selectAll()) {
            val parentName = file[FileDataObject.parent];
            if (parentName == fileLevel.value) {
                var fileData = File(file[FileDataObject.fileName]);
                fileData.content = file[FileDataObject.fileContent];
                fileData.isFolder = file[FileDataObject.isFolder];
                fileData.parent = file[FileDataObject.parent];
                fileList.add(fileData)
            }
        }
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        if (fileLevel.value != "root") {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                TextButton(
                    onClick = {
                        // get parents filename
                        transaction {
                            for (file in FileDataObject.selectAll()) {
                                if (file[FileDataObject.fileName] == fileLevel.value) {
                                    fileLevel.value = file[FileDataObject.parent]
                                }
                            }
                        }
                    },
                    modifier = Modifier.height(50.dp)
                ) {
                    // add back arrow here
                    Text(
                        text = "..",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 50.dp, vertical = 0.dp),
                        color = Color.Black,
                    )
                }
            }
        }

        for (targetFile in fileList) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                FileListItem(targetFile, fileLevel)
            }
        }
    }
}