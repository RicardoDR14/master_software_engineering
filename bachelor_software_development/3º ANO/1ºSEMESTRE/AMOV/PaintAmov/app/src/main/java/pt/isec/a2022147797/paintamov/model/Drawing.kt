package pt.isec.a2022147797.paintamov.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Point(val x: Float, val y: Float)
data class Line(val begin: Point, val end: Point, val color: ULong)
class Drawing {
    private val lines = mutableListOf<Line>()

    val _linesFlow = MutableStateFlow<List<Line>>(lines.toList())
    val linesFlow : StateFlow<List<Line>> = _linesFlow

    fun addLine(line: Line) {
        lines.add(line)
        _linesFlow.value = lines.toList()
    }
}