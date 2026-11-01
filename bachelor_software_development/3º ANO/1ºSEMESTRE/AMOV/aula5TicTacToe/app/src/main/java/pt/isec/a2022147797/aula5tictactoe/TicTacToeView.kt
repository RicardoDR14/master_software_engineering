package pt.isec.a2022147797.aula5tictactoe

import TicTacToeModel
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View


class TicTacToeView(context: Context) : View(context) {

    private var _board : Array<Array<TicTacToeModel.Players>>? = null
    public var board : Array<Array<TicTacToeModel.Players>>?
        get() = _board
        set(value) {
            _board = value
            invalidate()
        }

    fun interface OnPlayListener{
        fun play(y: Int,x:Int)
    }

    private var _onPlayListener :OnPlayListener? = null

    public val onPlayListener: OnPlayListener?
        get() = _onPlayListener

    fun setOnPlayListener(listener:OnPlayListener){
        _onPlayListener = listener
    }

    val BackGroundPaint = Paint(Paint.DITHER_FLAG or Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f,0f,width.toFloat(),height.toFloat(),BackGroundPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?: return super.onTouchEvent(event)
        if (event.action ==MotionEvent.ACTION_UP){
            val cellWith = width/3
            val cellHeight = height/3
            val y = (event.y/cellWith).toInt()
            val x = (event.x/cellHeight).toInt()
            _onPlayListener?.play(y, x)
        }
        return true
    }
}