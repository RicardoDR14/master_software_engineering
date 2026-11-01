package pt.isec.a2022147797.ex10.model;

public class HangmanModel {
    private static final int MAX_ERRORS = 7;
    private static final String HIDDEN_CHARACTER = ".";

    private String wordToGuess;
    private StringBuffer currentSituation;
    private StringBuilder attempedCharacters;
    private int nAttempts, nFound;

    public HangmanModel() {
        initialize();
    }

    private void initialize() {
        int index = (int) (Math.random() * HangmanDictionary.getNumWords());
        wordToGuess = HangmanDictionary.getWord(index).toUpperCase();
        currentSituation = new StringBuffer(HIDDEN_CHARACTER.repeat(wordToGuess.length()));
        attempedCharacters = new StringBuilder();
        nAttempts = nFound = 0;
    }

    public void newGame() {
        initialize();
    }

    public String getWordToGuess() {
        if (!isConcluded())
            return null;
        return wordToGuess;
    }

    public String getCurrentSituation() {
        return currentSituation.toString();
    }

    public String getAttempedCharacters() {
        return attempedCharacters.toString();
    }

    public int getnAttempts() {
        return nAttempts;
    }

    public int getnFound() {
        return nFound;
    }

    public static int getMaxErrors() {
        return MAX_ERRORS;
    }

    public int getnErrors() {
        return nAttempts - nFound;
    }

    public boolean isWordFound() {
        return wordToGuess.equalsIgnoreCase(currentSituation.toString());
    }

    public boolean isConcluded() {
        return isWordFound() || getnErrors() >= MAX_ERRORS;
    }

    public boolean tryOption(String option) {
        if (isConcluded() ||option == null || option.isBlank())
            return false;
        nAttempts++;
        option = option.toUpperCase();
        if (option.length()>1) {
            if (wordToGuess.equalsIgnoreCase(option)) {
                currentSituation = new StringBuffer(wordToGuess);
                nFound++;
                return true;
            }
            return false;
        }

        if (attempedCharacters.toString().contains(option)){
            nAttempts++;
            return false;
        }

        if (wordToGuess.contains(option)) {
            for (int i = 0; i < wordToGuess.length(); i++) {
                if (wordToGuess.charAt(i) == option.charAt(0)) {
                    currentSituation.setCharAt(i, option.charAt(0));
                    nFound++;
                }
            }
            return true;
        }

        attempedCharacters.append(option);
        return false;
    }
}
