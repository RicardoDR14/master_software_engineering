import "./Box.css"

export const Box = ({ value, onBoxClick }) => {
    
    return (
        <button className={'box ' + value} onClick={onBoxClick}>{value}</button>
    )
}


