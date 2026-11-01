package pt.isec.pa.library.model;

import java.util.*;

public class LibraryMap extends Library {
    private Map<Integer,Book> books;

    public LibraryMap(String name) {
        super(name);
        books = new HashMap<>();
    }

    @Override
    public Collection<Book> getBooks() {
        return books.values();
    }

    @Override
    public int addBook(Book newBook) {
        if (books.values().contains(newBook))
            return -1;
        books.put(newBook.getId(), newBook);
        return newBook.getId();
    }

    @Override
    public Book findBook(String title) throws CloneNotSupportedException {
        for(Book book : books.values())
            if (book.getTitle().equalsIgnoreCase(title))
                return book.clone();
        return null;
    }

    @Override
    public Book findBook(int id) throws CloneNotSupportedException {
        Book book = books.get(id);
        return  book != null ? book.clone() : null;

    }

    @Override
    public boolean removeBook(String title) {
        for(Book book : books.values())
            if (book.getTitle().equalsIgnoreCase(title))
                return books.remove(book.getId())!=null;
        return false;
    }

    @Override
    public boolean removeBook(int id) {
        return books.remove(id) != null;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(String.format("Library %s:\n",name));
        for(Book book : books.values())
            output.append(String.format("  - %s\n",book));
        return output.toString();
    }
}
