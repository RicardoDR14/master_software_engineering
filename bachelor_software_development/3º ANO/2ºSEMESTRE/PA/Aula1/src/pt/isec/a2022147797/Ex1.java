package pt.isec.a2022147797;

public class Ex1 {
    public static void main(String[] args) {
        int secretNumber = (int) (Math.random() * 101);
        int userGuess;

        do {
            System.out.print("Guess a number between 0 and 100: ");
            userGuess = new java.util.Scanner(System.in).nextInt();

            if (userGuess == secretNumber) {
                System.out.println("You guessed it!");
            } else if (userGuess < secretNumber) {
                System.out.println("Too low!");
            } else {
                System.out.println("Too high!");
            }
        } while (userGuess != secretNumber);
    }
}
