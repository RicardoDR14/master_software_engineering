package pt.isec.a2022147797.ex11.model;

import java.util.Arrays;

public class Report {
    private static final int MAX_AUTHORS = 4;

    private String title;
    private String [] authors;
    private StringBuilder text;

    public Report(String title) {
        this.title = title;
        authors = new String[MAX_AUTHORS];
        text = new StringBuilder();
    }

    public boolean addAuthor(String author) {
        authors[nAuthors++] = author;
        return
    }

    public void removeAuthor(String author) {
        int index = -1;
        for (int i = 0; i < authors.length; i++) {
            if (authors[i].equals(author)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            String[] newAuthors = Arrays.copyOf(authors, authors.length - 1);
            System.arraycopy(authors, index + 1, newAuthors, index, authors.length - index - 1);
            authors = newAuthors;

        }
    }

    public void addText(String text) {
        this.text.append(text);
    }

    public void capitalizeSentences() {
    }

    public int getNumberOfWords() {
        String [] words = text.toString().split("[\\s,\\.;!?\\t\\n]+");
        return words.length;
    }

    public int getNumberOfOccurrences(String word) {

    }
}
