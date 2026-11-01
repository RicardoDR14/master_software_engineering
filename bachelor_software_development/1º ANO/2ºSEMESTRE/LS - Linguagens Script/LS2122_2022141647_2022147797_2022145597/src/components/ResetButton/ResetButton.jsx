import React from 'react'
import "./ResetButton.css"

export const ResetButton = ({setBoards, setOScore, setXScore}) => {

  function reset(){
    window.location.reload()
  }

  return (
    <button className='reset-btn' onClick={reset}>Reset</button>
  )
}
