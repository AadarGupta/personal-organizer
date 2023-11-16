package files

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilePreview(
    editState: MutableState<String>,
    fileItemIdx: Int,
    fileVM: FileViewModel
) {
    var fileItem = fileVM.getFileByIdx(fileItemIdx)
    var query = remember { mutableStateOf(fileItem.fileContent) }

    Window(onCloseRequest = { editState.value = "closed" })
    {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                Text(
                    text = fileItem.fileName,
                    fontSize = 40.sp,
                    modifier = Modifier.padding(horizontal = 15.dp).padding(top = 10.dp, bottom = 0.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )

                Icon(
                    imageVector = Icons.Filled.Done, contentDescription = "Save",
                    modifier = Modifier
                        .clickable {
                            fileVM.editFileContent(fileItem, query.value)
                            editState.value = "closed"
                        }
                        .height(40.dp)
                        .padding(horizontal = 30.dp),
                    tint = Color.Blue
                )

            }

            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 100.dp)
            ) {
                Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = Color.Black)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Need to see how to comprehend query as Markdown
                    var lines = query.value.lines()

                    lines.forEach {
                        var string = it.trim()

                        // Create headings
                        if(string.length > 6 && string.substring(0,6) == "######"){
                            H6(string.substring(6).trim())
                        } else if(string.length > 5 && string.substring(0,5) == "#####") {
                            H5(string.substring(5).trim())
                        } else if(string.length > 4 && string.substring(0,4) == "####") {
                            H4(string.substring(4).trim())
                        } else if(string.length > 3 && string.substring(0, 3) == "###") {
                            H3(string.substring(3).trim())
                        } else if(string.length > 2 && string.substring(0, 2) == "##") {
                            H2(string.substring(2).trim())
                        } else if(string.length > 1 && string[0] == '#') {
                            H1(string.substring(1).trim())
                        } // Create bullet points
                        else if(string.length > 1 && string[0] == '-') {
                        PointForm(string.substring(1).trim())
                        } else {
                            TextInline(string)
                        }
                    }
                }
            }

        }
    }
}