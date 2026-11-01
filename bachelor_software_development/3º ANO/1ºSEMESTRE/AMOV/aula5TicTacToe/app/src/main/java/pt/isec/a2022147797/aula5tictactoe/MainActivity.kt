package pt.isec.a2022147797.aula5tictactoe

import TicTacToeModel
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

class TicTacToeViewModel:ViewModel(){
    private val game = TicTacToeModel()

    val winsP1=MutableLiveData<Int>(0)
    val winsP2 =MutableLiveData<Int>(0)
    val currentPlayer = MutableLiveData<TicTacToeModel.Players>()
    var isFinished = false
    val board = MutableLiveData<Array<Array<TicTacToeModel.Players>>>()

    init {
        update()
    }

    fun update(){
        winsP1.value=game.winsP1
        winsP2.value =game.winsP2
        currentPlayer.value = game.currentPlayer
        isFinished = game.gameFinished
        board.postValue(game.getBoard())
    }


    fun start(){
        game.start()
        update()
    }

    fun reset(){
        game.reset()
        update()
    }

    fun play(y : Int, x : Int){
        game.play(y, x)
        update()
    }

}


class MainActivity : AppCompatActivity() {
    val viewModel : TicTacToeViewModel by viewModels()

    lateinit var tttView : TicTacToeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tttView = TicTacToeView(this)
        findViewById<FrameLayout>(R.id.flBoard).addView(tttView)

        viewModel.winsP1.observe(this){ value ->
            findViewById<TextView>(R.id.pointsP1).text =
                String.format(Locale.getDefault(),"%03d",value)
        }

        viewModel.winsP2.observe(this){value ->
            findViewById<TextView>(R.id.pointsP2).text =
                String.format(Locale.getDefault(),"%03d",value)

        }

        viewModel.currentPlayer.observe(this){
                value ->
            findViewById<TextView>(R.id.pointsP1).setBackgroundColor(
                if (value ==TicTacToeModel.Players.P1)
                Color.CYAN else Color.TRANSPARENT
            )

            findViewById<TextView>(R.id.pointsP2).setBackgroundColor(
                if (value ==TicTacToeModel.Players.P2)
                    Color.CYAN else Color.TRANSPARENT
            )
        }

        findViewById<Button>(R.id.start).setOnClickListener{
            viewModel.start()
        }

        viewModel.board.observe(this){ newBoard ->
            findViewById<Button>(R.id.start).isEnabled = viewModel.isFinished
            tttView.board = newBoard
        }

        tttView.setOnPlayListener{y,x ->
            viewModel.play(y, x)
        }
    }
}
