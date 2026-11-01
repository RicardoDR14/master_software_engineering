import kotlin.random.Random

class TicTacToeModel {
    enum class Players {
        None, P1, P2
    }

    private lateinit var board: Array<Array<Players>>
    private var _winsP1 = 0
    private var _winsP2 = 0
    private var _currentPlayer = Players.None
    private var _ctrl = 9

    public val currentPlayer : Players
        get() = _currentPlayer

    public val winsP1 : Int
        get() = _winsP1

    public val winsP2 : Int
        get() = _winsP2

    public val Winner : Players
        get() = checkForWinner()

    var _gameFinished = true
    public val gameFinished : Boolean
        get() = _gameFinished

    init {
        start()
    }

    public fun start() {
        board = Array(3) { r -> Array(3) { Players.None } }
        _gameFinished = false
        _ctrl = 9
        _currentPlayer = if (Random.nextBoolean()) Players.P1 else Players.P2
    }

    public fun reset() {
        _winsP1 = 0
        _winsP2 = 0
        start()
    }

    public fun get(y: Int, x: Int): Players {
        return board[y][x]
    }

    public fun play(y: Int, x: Int): Boolean {
        if (_gameFinished || _currentPlayer == Players.None || board[y][x] != Players.None)
            return false
        board[y][x] = _currentPlayer
        _ctrl--
        when (checkForWinner()) {
            Players.P1 -> {_winsP1++ ; _gameFinished = true }
            Players.P2 -> {_winsP2++ ; _gameFinished = true }
            else -> {_gameFinished = _ctrl < 1}
        }
        if (!_gameFinished)
            _currentPlayer = if (_currentPlayer == Players.P1) Players.P2 else Players.P1
        return true
    }

    private fun checkForWinner(): Players {
        // Check rows
        for (i in 0 until board.size) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != Players.None) {
                return board[i][0]
            }
        }

        // Check columns
        for (i in 0 until board[0].size) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != Players.None) {
                return board[0][i]
            }
        }

        // Check diagonals
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != Players.None) {
            return board[0][0]
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != Players.None) {
            return board[0][2]
        }

        // No winner
        return Players.None
    }

    public fun getBoard() : Array<Array<Players>> {
        return board.map { row -> row.copyOf() }.toTypedArray()
    }
}