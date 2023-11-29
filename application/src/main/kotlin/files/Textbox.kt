package files

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import java.awt.Desktop
import java.net.URI
import java.net.URL
import javax.imageio.ImageIO


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
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 3.dp),
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun PointForm(string: String) {
    Text(
        text = "• $string",
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp),
        color = Color.Black,
        fontSize = 16.sp,
    )
}

@Composable
fun CodeLine(string: String) {
    Text(
        text = string,
        style = TextStyle(
            fontSize = 16.sp,
            fontFamily = FontFamily.Monospace
        ),
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 5.dp) // Padding applied first
            .background(Color.DarkGray)
            .padding(horizontal = 4.dp, vertical = 2.dp),
        color = Color.Yellow
    )
}


@Composable
fun TextInline(string: String) {
    Text(
        buildAnnotatedString {
            var bold = false;
            var italics = false;
            var prev = "";

            for (i in string.indices) {
                var curr = string[i].toString()

                if(curr == "*") {
                    if(prev == "*") {
                        bold = !bold
                    } else {
                        italics = !italics
                    }
                }
                prev = curr

                if(curr != "*") {
                    if(bold) {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(curr)
                        }
                    } else if (italics) {
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(curr)
                        }
                    } else {
                        append(curr)
                    }
                }

            }
        },
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp)
    )
}

@Composable
fun ClickableURL(url: String) {
    var link = url
    if (url.substring(0, 5) != "https") {
        link = "https://${url}"
    }
    val annotatedText = buildAnnotatedString {
        pushStringAnnotation(tag = "URL", annotation = link)
        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
            append(link)
        }
        pop()
    }

    fun openLink(url: String) {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(URI(url))
        }
    }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    openLink(annotation.item)
                }
        },
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp),
    )
}

@Composable
fun ShowImage(url: String) {
    Image(
        bitmap = ImageIO.read(URL(url)).toComposeImageBitmap(),
        contentDescription = null,
        modifier = Modifier.height(50.dp).padding(horizontal = 15.dp, vertical = 2.dp)
    )
}



