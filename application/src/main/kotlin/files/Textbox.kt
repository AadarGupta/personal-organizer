package files

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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


// Generates different styled simplified markdown

@Composable
fun H1(string: String) {
    // Generates H1
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
    // Generates H2
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
    // Generates H3
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
    // Generates H4
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
    // Generates H5
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
    // Generates H6
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
    // Generates point with a prevailing dot
    Text(
        text = "• $string",
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp),
        color = Color.Black,
        fontSize = 16.sp,
    )
}

@Composable
fun CodeLine(string: String) {
    // Generates and formats code lines
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
    // Inline text (handles any in text bolding and italics)
    Text(
        // Builds an annotated string
        buildAnnotatedString {
            // By default, no bold or italics
            var bold = false;
            var italics = false;
            var prev = "";

            // Loop through each string
            for (i in string.indices) {
                // Get the current character
                var curr = string[i].toString()

                // If the current character is *
                if(curr == "*") {
                    // If current and previous are * => reverse bold truth value
                    if(prev == "*") {
                        bold = !bold
                    } // If only current is * => reverse italics truth value
                    else {
                        italics = !italics
                    }
                }
                // Update prev to the current value
                prev = curr

                // If the current value is not *
                if(curr != "*") {
                    // If need to bold, apply bold and append
                    if(bold) {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(curr)
                        }
                    } // If need to italics, apply italics and append
                    else if (italics) {
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(curr)
                        }
                    } // Append normal text with no bold or italics
                    else {
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
    // Generates a clickable url

    // Sets link to the url
    var link = url

    // If there is no https:// in front of the link, add it
    if (url.substring(0, 5) != "https") {
        link = "https://${url}"
    }

    // Builds an annotated string
    val annotatedText = buildAnnotatedString {
        // Adds a string annotation of the URL
        pushStringAnnotation(tag = "URL", annotation = link)
        // Styles the link as a link
        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
            append(link)
        }
        pop()
    }

    // Opens the link
    fun openLink(url: String) {
        // If the link is clicked and on desktop, open in default browser
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(URI(url))
        }
    }

    // Generates a clickable text using the url
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
    // Generates an image using the url
    Image(
        bitmap = ImageIO.read(URL(url)).toComposeImageBitmap(),
        contentDescription = null,
        modifier = Modifier.height(50.dp).padding(horizontal = 15.dp, vertical = 2.dp)
    )
}



