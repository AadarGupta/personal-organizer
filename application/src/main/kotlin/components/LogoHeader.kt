package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LogoHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 30.dp),
    ) {
        // logo
        Image(
            painter = painterResource("mypoLogo.png"),
            contentDescription = "app logo",
            modifier = Modifier.padding(10.dp, 4.dp)
        )

        // title
        Text(
            text = "My Personal Organizer",
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(start = 20.dp, top = 30.dp),
            color = Color.DarkGray,
        )

    }
}