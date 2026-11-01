package pt.isec.a2022147797;

public class Ex2 {
    public static void main(String[] args) {

        int lowerBound = 1;
        int upperBound = 100;
        int attempts = 0;

        while (lowerBound <= upperBound) {
            int guess = (lowerBound + upperBound) / 2;
            attempts++;
            System.out.println("I'm thinking of " + guess);
            System.out.print("Is that correct? (CORRECT, MINE IS LOWER, MINE IS HIGHER): ");
            String answer = new java.util.Scanner(System.in).nextLine();

            if (answer.equalsIgnoreCase("CORRECT")) {
                System.out.println("I guessed it in " + attempts + " attempt(s)!");
                return;
            } else if (answer.equalsIgnoreCase("MINE IS LOWER")) {
                upperBound = guess - 1;
            } else if (answer.equalsIgnoreCase("MINE IS HIGHER")) {
                lowerBound = guess + 1;
            }
        }

        System.out.println("I couldn't guess your number!");
    }
}
