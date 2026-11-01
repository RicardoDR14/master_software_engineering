package pt.isec.a2022147797.ex13.model;

import java.util.List;

public class Book implements Cloneable {
    private static int idCounter = 0;

    private static int getNewId() {
        return ++idCounter;
    }

    public static Book dummyBook(String title) {
        Book book = new Book(title, List.of());
        --idCounter;
        return book;
    }

    private final int id;
    private String title;
    private List<String> authors;

    public Book(String title, List<String> authors) {
        this.id = getNewId();
        this.title = title;
        this.authors = List.copyOf(authors);
    }

    public Book(String title, String ... authors) {
        this(title, List.of(authors));
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return List.copyOf(authors);
    }

    public void setAuthors(List<String> authors) {
        this.authors = List.copyOf(authors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return book.title.equalsIgnoreCase(this.title);
    }

    @Override
    public int hashCode() {
        return title.toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        String strAuthors = authors.toString();
        return String.format("[%d] %s - %s", id, title, strAuthors.substring(1, strAuthors.length()-1));
    }

    @Override
    public Book clone() throws CloneNotSupportedException {
        Book newBook = (Book) super.clone();
        //newBook.id = getNewId();
        newBook.authors = List.copyOf(authors);
        return newBook;
    }
}
