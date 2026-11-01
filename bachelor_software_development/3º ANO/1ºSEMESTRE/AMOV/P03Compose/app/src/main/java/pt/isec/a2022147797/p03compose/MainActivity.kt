package pt.isec.a2022147797.p03compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isec.a2022147797.p03compose.ui.theme.P03ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            P03ComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var counter by remember { mutableStateOf(0) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Yellow),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        for (i in 1..5) {
            Text(
                text = "${counter+i}Hello $name $i!",
                modifier = Modifier
            )
        }
        Row(
            modifier = Modifier
                .background(Color.Cyan)
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Green)
                .padding(16.dp),

            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "Hello $name!",
                modifier = Modifier
            )
            Text(
                text = "Hello $name!",
                modifier = Modifier
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                counter++
                Log.i("teste","Clicou ${counter} vezes")
            }
        ) {
            Text("Carrega")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    P03ComposeTheme {
        Greeting("Android")
    }
}