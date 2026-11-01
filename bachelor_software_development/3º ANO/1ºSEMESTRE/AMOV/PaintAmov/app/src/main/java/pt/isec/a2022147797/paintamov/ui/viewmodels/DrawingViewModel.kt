package pt.isec.a2022147797.paintamov.ui.viewmodels

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import pt.isec.a2022147797.paintamov.model.Drawing
import pt.isec.a2022147797.paintamov.model.Line
import pt.isec.a2022147797.paintamov.model.Point

data class CanvasLine(val begin: Offset, val end: Offset, val color: Color)
class DrawingViewModel : ViewModel() {
    private val drawing = Drawing()
    private val _linesFlow = MutableStateFlow<List<CanvasLine>>(emptyList())
    val linesFlow: MutableStateFlow<List<CanvasLine>> = _linesFlow
    init {
        viewModelScope.launch {
            drawing.linesFlow.collect { lines ->
                _linesFlow.emit(lines.map { line ->
                    CanvasLine( Offset(line.begin.x, line.begin.y),
                        Offset(line.end.x, line.end.y), Color(line.color) )
                })
            }
        }
    }

    fun addLine(
        start: Offset,
        end: Offset,
        color : Color
    ) {
        drawing.addLine(
            Line(
                Point(start.x, start.y),
                Point(end.x, end.y),
                color.value
            )
        )
    }
}