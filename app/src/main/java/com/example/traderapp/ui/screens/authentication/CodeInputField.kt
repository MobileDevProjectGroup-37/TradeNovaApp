import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traderapp.ui.theme.LightOffline

@Composable
fun CodeInputField(
    onCodeChange: (String) -> Unit
) {
    var codeList by remember { mutableStateOf(List(6) { "" }) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        codeList.forEachIndexed { index, digit ->
            BasicTextField(
                value = digit,
                onValueChange = { newValue ->
                    if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                        // Update local state
                        val updatedList = codeList.toMutableList().apply {
                            this[index] = newValue
                        }
                        codeList = updatedList


                        val joinedCode = updatedList.joinToString("")

                        onCodeChange(joinedCode)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                modifier = Modifier
                    .size(48.dp)
                    .border(1.4.dp, LightOffline, RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}
