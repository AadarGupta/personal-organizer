package files

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

    var toCreate = remember { mutableStateOf(false) }
    var toDelete = remember{ mutableStateOf(false) }

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

        Row( modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp),) {
            TextButton(onClick = { toCreate.value = !toCreate.value }, modifier = Modifier.height(50.dp)) {
                if(toCreate.value) {
                    FileCreate(toCreate)
                }
                Image(
                    painter = painterResource("createFileIcon.png"),
                    contentDescription = "Create New File Icon",
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    text = "Create New File",
                    fontSize = 15.sp,
                    color = Color.Blue,
                )
            }

            TextButton(onClick = { toDelete.value = !toDelete.value }, modifier = Modifier.height(50.dp)) {
                /*if(toCreate.value) {
                    // Needs to be FileDelete which pulls a list of files and deletes the clicked ones
                    FileCreate(toCreate)
                }*/
                Image(
                    painter = painterResource("trashIcon.png"),
                    contentDescription = "Delete File Icon",
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    text = "Delete File",
                    fontSize = 15.sp,
                    color = Color.Red,
                )
            }
        }

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
