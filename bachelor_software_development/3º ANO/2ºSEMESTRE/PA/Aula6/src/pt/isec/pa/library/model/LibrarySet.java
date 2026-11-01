package pt.isec.pa.library.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LibrarySet extends Library {
    private Set<Book> books;

    public LibrarySet(String name) {
        super(name);
        books = new HashSet<>();
    }

    @Override
    public Set<Book> getBooks() {
        return books;
    }

    @Override
    public int addBook(Book newBook) {
        if (!books.add(newBook))
            return -1;
        return newBook.getId();
    }

    @Override
    public Book findBook(String title) throws CloneNotSupportedException {
        for(Book book : books)
            if (book.getTitle().equalsIgnoreCase(title))
                return book.clone();
        return null;
    }

    @Override
    public Book findBook(int id) throws CloneNotSupportedException {
        for(Book book : books)
            if (book.getId()==id)
                return book.clone();
        return null;
    }

    @Override
    public boolean removeBook(String title) {
        return books.remove(new Book(title,List.of()));
    }

    @Override
    public boolean removeBook(int id) {
        for(Book book : books)
            if (book.getId()==id)
                return books.remove(book);
        return false;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(String.format("Library %s:\n",name));
        for(Book book : books)
            output.append(String.format("  - %s\n",book));
        return output.toString();
    }
}
