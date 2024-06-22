package tenfen.rodolfo.javakeywordsgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.delay
import tenfen.rodolfo.javakeywordsgame.ui.theme.JavaKeywordsGameTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JavaKeywordsGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameScreen()
                }
            }
        }
    }
}

private val keywords = listOf("class", "int", "void", "if", "return")

@Preview
@Composable
private fun GameScreen() {
    var enteredKeywords by remember { mutableStateOf(emptyList<String>()) }

    Column(Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).padding(top = 8.dp)) {
            Score(current = enteredKeywords.size, maximum = keywords.size)

            Spacer(modifier = Modifier.fillMaxWidth().weight(1f))

            Timer(duration = 5.minutes)
        }

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).padding(top = 8.dp)) {
            KeywordInput(
                Modifier.fillMaxWidth()
            ) { validKeyword ->
                enteredKeywords += validKeyword
            }

            EnteredKeywords(
                Modifier.fillMaxWidth().padding(top = 8.dp),
                enteredKeywords
            )
        }
    }
}

@Preview
@Composable
private fun Score(current: Int = 0, maximum: Int = 0) {
    Text("$current / $maximum")
}

@Preview
@Composable
fun Timer(duration: Duration = 5.minutes, onReachedZero: () -> Unit = {}) {
    var remainingTime by remember {
        mutableStateOf(duration.inWholeSeconds.toDuration(DurationUnit.SECONDS))
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1.seconds)

            remainingTime -= 1.seconds

            if (remainingTime == Duration.ZERO) {
                onReachedZero()

                break
            }
        }
    }

    Text(remainingTime.toString())
}

@Preview
@Composable
fun KeywordInput(
    modifier: Modifier = Modifier,
    onValidKeywordEntered: (validKeyword: String) -> Unit = {},
) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = {
            text = it

            if (text in keywords) {
                onValidKeywordEntered(text)
                text = ""
            }
        },
        label = { Text("Keyword") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear keyword input",
                modifier = Modifier.clickable { text = "" })
        }
    )
}

@Composable
fun EnteredKeywords(modifier: Modifier = Modifier, keywords: List<String>) {
    LazyColumn(modifier) {
        items(items = keywords) {
            Text(text = it)
        }
    }
}
