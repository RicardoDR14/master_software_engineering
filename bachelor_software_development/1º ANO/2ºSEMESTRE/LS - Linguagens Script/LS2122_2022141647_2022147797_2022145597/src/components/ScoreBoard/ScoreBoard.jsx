import React, { useEffect, useState } from 'react'
import './ScoreBoard.css'

export const ScoreBoard = ({ winner, xIsNext, boards, setBoards }) => {
    const [xPlayer, setXPlayer] = useState("Jogador X");
    const [oPlayer, setOPlayer] = useState("Jogador O");

    const [xTimer, setXTimer] = useState(600);
    const [oTimer, setOTimer] = useState(600);
    const [xTimerOn, setXTimerOn] = useState(xIsNext);
    const [oTimerOn, setOTimerOn] = useState(!xIsNext);

    useEffect(() => {
        let interval = null;
        if (xTimerOn) {
            interval = setInterval(() => {
                setXTimer(xTimer => xTimer - 1);
            }, 1000);
        } else {
            clearInterval(interval);
        }
        return () => clearInterval(interval);
    }, [xTimerOn, xTimer]);

    useEffect(() => {
        let interval = null;
        if (oTimerOn) {
            interval = setInterval(() => {
                setOTimer(oTimer => oTimer - 1);
            }, 1000);
        } else {
            clearInterval(interval);
        }
        return () => clearInterval(interval);
    }, [oTimerOn, oTimer]);

    useEffect(() => {
        setXTimerOn(xIsNext);
        setOTimerOn(!xIsNext);
    }, [xIsNext]);

    useEffect(() => {
        if (xTimer <= 0 || oTimer <= 0) {
            setBoards("finished");
        }
    }, [xTimer, oTimer]);

    if (winner === 'X') {
        winner = xPlayer    
    } else if (winner === 'O') {
        winner = oPlayer
    } else {
        winner = ''
    }

    const handleChangeX = (event) => {
        setXPlayer(event.target.value);
    }
    const handleChangeO = (event) => {
        setOPlayer(event.target.value);
    }

    return (
        <div className={'scoreboard ' + winner}>
            <div className='players'>
                <input type="text" className='input' value={xPlayer} onChange={handleChangeX} placeholder='Jogador X' />
                <input type="text" className='input' value={oPlayer} onChange={handleChangeO} placeholder='Jogador O' />
            </div>
            <div className="score">
                <span className={`score x-score ${!xIsNext && "inactive"}`}>{xPlayer}</span>
                <span className={`score o-score ${xIsNext && "inactive"}`}>{oPlayer}</span>
            </div>
            <div className='winner'>
                <p>{!winner && xTimer > 0 && oTimer > 0 ? '' : 'Vencedor: '+ (winner ? winner : '')}
                    {xTimer <= 0 ? oPlayer + ' ganhou' : ''}
                    {xTimer > 0 && boards !== "finished" && (
                        <p>Tempo de {xPlayer}: {
                            xTimer / 60 >= 10 ? Math.floor(xTimer / 60) : '0' + Math.floor(xTimer / 60)
                        }
                            :{
                                xTimer % 60 >= 10 ? xTimer % 60 : '0' + xTimer % 60
                            }
                        </p>
                    )}
                    {oTimer <= 0 ? xPlayer + ' ganhou' : ''}
                    {oTimer > 0 && boards !== "finished" && (
                        <p>Tempo de {oPlayer}: {
                            oTimer / 60 >= 10 ? Math.floor(oTimer / 60) : '0' + Math.floor(oTimer / 60)
                        }
                            :{
                                oTimer % 60 >= 10 ? oTimer % 60 : '0' + oTimer % 60
                            }
                        </p>
                    )}
                </p>
            </div>
        </div>



    )
}
