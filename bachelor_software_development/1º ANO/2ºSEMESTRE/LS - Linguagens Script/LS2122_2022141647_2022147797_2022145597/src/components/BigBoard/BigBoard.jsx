import React from 'react'
import { Board } from '../Board/Board'
import "./BigBoard.css"

export const BigBoard = ({boards, setBoards, winner, setWinner, oScore, setOScore, xScore, setXScore, xIsNext, setXIsNext}) => {

  function calculateWinner(boards) {
    const wins = [
      [0, 1, 2],
      [3, 4, 5],
      [6, 7, 8],
      [0, 3, 6],
      [1, 4, 7],
      [2, 5, 8],
      [0, 4, 8],
      [2, 4, 6],
    ];
    for (let i = 0; i < wins.length; i++) {
      const [a, b, c] = wins[i];
      if (boards[a] && boards[a] === boards[b] && boards[a] === boards[c]) {
        return boards[a];
      }
    }
    return null;
  }

  return (
    <div className={'bigboard ' + calculateWinner(boards)}>
      <div className="bigboard-row">
        <Board index={0} key={0} boards={boards} setBoards={setBoards} setXIsNext={setXIsNext} xIsNext={xIsNext} setWinner={setWinner} oScore={oScore} setOScore={setOScore} xScore={xScore} setXScore={setXScore}/>
        <Board index={1} key={1} boards={boards} setBoards={setBoards} setXIsNext={setXIsNext} xIsNext={xIsNext} setWinner={setWinner} oScore={oScore} setOScore={setOScore} xScore={xScore} setXScore={setXScore}/>
        <Board index={2} key={2} boards={boards} setBoards={setBoards} setXIsNext={setXIsNext} xIsNext={xIsNext} setWinner={setWinner} oScore={oScore} setOScore={setOScore} xScore={xScore} setXScore={setXScore}/>
      </div>
      <div className="bigboard-row">
        <Board index={3} key={3} boards={boards} setBoards={setBoards} setXIsNext={setXIsNext} xIsNext={xIsNext} setWinner={setWinner} oScore={oScore} setOScore={setOScore} xScore={xScore} setXScore={setXScore}/>
        <Board index={4} key={4} boards={boards} setBoards={setBoards} setXIsNext={setXIsNext} xIsNext={xIsNext} setWinner={setWinner} oScore={oScore} setOScore={setOScore} xScore={xScore} setXScore={setXScore}/>
        <Board index={5} key={5} boards={boards} setBoards={setBoards} setXIsNext={setXIsNext} xIsNext={xIsNext} setWinner={setWinner} oScore={oScore} setOScore={setOScore} xScore={xScore} setXScore={setXScore}/>
      </div>
      <div className="bigboard-row">
        <Board index={6} key={6} boards={boards} setBoards={setBoards} setXIsNext={setXIsNext} xIsNext={xIsNext} setWinner={setWinner} oScore={oScore} setOScore={setOScore} xScore={xScore} setXScore={setXScore}/>
        <Board index={7} key={7} boards={boards} setBoards={setBoards} setXIsNext={setXIsNext} xIsNext={xIsNext} setWinner={setWinner} oScore={oScore} setOScore={setOScore} xScore={xScore} setXScore={setXScore}/>
        <Board index={8} key={8} boards={boards} setBoards={setBoards} setXIsNext={setXIsNext} xIsNext={xIsNext} setWinner={setWinner} oScore={oScore} setOScore={setOScore} xScore={xScore} setXScore={setXScore}/>
      </div>
    </div>
  )
}
