import React, { useState } from 'react';
import './App.css';
import { BigBoard } from './components/BigBoard/BigBoard';
import { ScoreBoard } from './components/ScoreBoard/ScoreBoard';
import { ResetButton } from './components/ResetButton/ResetButton';
import { Header } from './components/Header/Header';

function App() {
  const [boards, setBoards] = useState(Array(9).fill(null));
  const [winner, setWinner] = useState(null);
  const [xIsNext, setXIsNext] = useState(true);


  return (
    <div className="App">
      <>
        <Header />
        <ScoreBoard winner={winner} xIsNext={xIsNext} boards={boards} setBoards={setBoards} />
        <BigBoard boards={boards} setBoards={setBoards} winner={winner} setWinner={setWinner} xIsNext={xIsNext} setXIsNext={setXIsNext} />
        <ResetButton />
      </>
    </div>
  );
}


export default App;
