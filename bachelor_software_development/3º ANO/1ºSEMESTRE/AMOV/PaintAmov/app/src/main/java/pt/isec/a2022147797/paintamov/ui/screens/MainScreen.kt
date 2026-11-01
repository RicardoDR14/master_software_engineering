package pt.isec.a2022147797.paintamov.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import pt.isec.a2022147797.paintamov.ui.viewmodels.DrawingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: DrawingViewModel,
    modifier: Modifier = Modifier
) {
    var currentColor by remember { mutableStateOf(Color.Black) }
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("PaintAmov", color = currentColor)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(255, 255, 255)
                ),
                actions = {
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        onClick = { currentColor = Color.Red },
                        modifier = Modifier
                            .padding(4.dp)
                            .width(40.dp)
                            .aspectRatio(1f)
                    ) {}
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Green),
                        onClick = { currentColor = Color.Green },
                        modifier = Modifier
                            .padding(4.dp)
                            .width(40.dp)
                            .aspectRatio(1f)
                    ) {}
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Blue),
                        onClick = { currentColor = Color.Blue },
                        modifier = Modifier
                            .padding(4.dp)
                            .width(40.dp)
                            .aspectRatio(1f)
                    ) {}
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Black),
                        onClick = { currentColor = Color.Black },
                        modifier = Modifier
                            .padding(4.dp)
                            .width(40.dp)
                            .aspectRatio(1f)
                    ) {}
                }
            )
        }
    ) { innerPadding ->
        val linesFlow by viewModel.linesFlow.collectAsState()
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pointerInput(Unit){
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        viewModel.addLine(
                            change.position-dragAmount,
                            change.position,
                            currentColor
                        )
                    }
                }
        ) {
            linesFlow.forEach { line ->
                drawLine(
                    start = line.begin,
                    end = line.end,
                    color = line.color,
                    strokeWidth = 10f
                )
            }
        }
    }
}