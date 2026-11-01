package pt.isec.a2022147797.ex10;

import pt.isec.a2022147797.ex10.model.HangmanModel;
import pt.isec.a2022147797.ex10.ui.HangmanUI;

public class HangmanGame {
    public static void main(String[] args) {
        HangmanModel game = new HangmanModel();
        HangmanUI gameUI = new HangmanUI(game);
        gameUI.play();
    }
}
