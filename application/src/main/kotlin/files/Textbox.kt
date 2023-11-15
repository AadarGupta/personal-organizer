package files

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp


@Composable
fun H1(string: String) {
    Text(
        text = string,
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
        color = Color.Black,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun H2(string: String) {
    Text(
        text = string,
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 7.dp),
        color = Color.Black,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun H3(string: String) {
    Text(
        text = string,
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
        color = Color.Black,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun H4(string: String) {
    Text(
        text = string,
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 4.dp),
        color = Color.Black,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun H5(string: String) {
    Text(
        text = string,
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 3.dp),
        color = Color.Black,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun H6(string: String) {
    Text(
        text = string,
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun PointForm(string: String) {
    Text(
        text = "• $string",
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
    )
}


