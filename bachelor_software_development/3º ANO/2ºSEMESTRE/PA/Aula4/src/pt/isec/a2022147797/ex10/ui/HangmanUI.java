package pt.isec.a2022147797.ex10.ui;

import pt.isec.a2022147797.ex10.model.HangmanModel;

import java.util.Scanner;

public class HangmanUI {
    HangmanModel game;
    Scanner sc;
    public HangmanUI(HangmanModel game) {
        this.game = game;
    }

    public void play() {
        sc = new Scanner(System.in);

        while (!game.isConcluded()) {
            System.out.println("\nCurrent situation: " + game.getCurrentSituation());  // mostrar as letras descobertas
            // se a palavra for CAFE
            // inicialmente deverá mostrar:  ....
            System.out.println("Number of attempts: " + game.getnAttempts()); // inicio: 0
            System.out.printf("Errors: %d (máx.: %d)\n", game.getnErrors(), HangmanModel.getMaxErrors());
            System.out.println("Attempted characteres: " + game.getAttempedCharacters());

            System.out.print("\nSuggestion: ");

            String option = sc.nextLine().trim();

            if (option.length()>0)
                game.tryOption(option);
        }

        if (game.isWordFound())
            System.out.printf("Congratulations! You guessed the word %s in %d attempts\n",
                    game.getWordToGuess(), game.getnAttempts());
        else
            System.out.println("Game over!!! The word to guess was: "+ game.getWordToGuess());
    }
}
