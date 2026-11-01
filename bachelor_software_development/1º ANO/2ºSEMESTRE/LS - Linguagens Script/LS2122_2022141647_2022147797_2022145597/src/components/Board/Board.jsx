import React, { useState } from 'react'
import "./Board.css"
import { Box } from '../Box/Box'

export const Board = ({ boards, setBoards, setWinner = null, index, setXIsNext, xIsNext , oScore, setOScore, xScore, setXScore}) => {
    const [boxes, setBoxes] = useState(Array(9).fill(null));
    const [isDraw, setIsDraw] = useState(false);
    /* const [xIsNext, setXIsNext] = useState(true); */

    function calculateWinner(boxes) {
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
            if (boxes[a] && boxes[a] === boxes[b] && boxes[a] === boxes[c]) {
                return boxes[a];
            }
        }
        return null;
    }

    function handleClick(i) {
        if (calculateWinner(boxes) || boxes[i] || calculateWinner(boards)) {
            return;
        }
        const nextBoxes = boxes.slice();
        if (xIsNext) {
            nextBoxes[i] = 'X';
        } else {
            nextBoxes[i] = 'O';
        }
        setBoxes(nextBoxes);
        setXIsNext(!xIsNext);

        if (isBoardFull(nextBoxes)) {
            setIsDraw(true);
        }
    }

    const winner = calculateWinner(boxes);

    if (winner) {
        boards[index] = winner;
    } else if (isDraw) {
        boards[index] = 'Empate!';
    }

    setWinner(calculateWinner(boards));
    setBoards(boards);



    function isBoardFull(boxes) {
        return boxes.every((box) => box !== null);
    }

    return (
        <div className={'board ' + calculateWinner(boxes)}>
            <div className="board-row">
                <Box value={boxes[0]} onBoxClick={() => handleClick(0)} />
                <Box value={boxes[1]} onBoxClick={() => handleClick(1)} />
                <Box value={boxes[2]} onBoxClick={() => handleClick(2)} />
            </div>
            <div className="board-row">
                <Box value={boxes[3]} onBoxClick={() => handleClick(3)} />
                <Box value={boxes[4]} onBoxClick={() => handleClick(4)} />
                <Box value={boxes[5]} onBoxClick={() => handleClick(5)} />
            </div>
            <div className="board-row">
                <Box value={boxes[6]} onBoxClick={() => handleClick(6)} />
                <Box value={boxes[7]} onBoxClick={() => handleClick(7)} />
                <Box value={boxes[8]} onBoxClick={() => handleClick(8)} />
            </div>
        </div>
    )
}
