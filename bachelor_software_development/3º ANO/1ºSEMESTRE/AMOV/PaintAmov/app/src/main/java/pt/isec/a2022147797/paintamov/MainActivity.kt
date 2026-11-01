package pt.isec.a2022147797.paintamov

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.isec.a2022147797.paintamov.ui.screens.MainScreen
import pt.isec.a2022147797.paintamov.ui.theme.PaintAmovTheme
import pt.isec.a2022147797.paintamov.ui.viewmodels.DrawingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaintAmovTheme {
                MainScreen(viewModel())
            }
        }
    }
}

