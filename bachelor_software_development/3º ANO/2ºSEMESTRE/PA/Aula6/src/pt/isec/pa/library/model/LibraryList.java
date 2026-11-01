package pt.isec.pa.library.model;

import java.util.ArrayList;
import java.util.List;

public class LibraryList extends Library {
    private List<Book> books;

    public LibraryList(String name) {
        super(name);
        books = new ArrayList<>();
    }

    @Override
    public List<Book> getBooks() {
        return books;
    }

    @Override
    public int addBook(Book newBook) {
        if (books.contains(newBook))
            return -1;
        books.add(newBook);
        return newBook.getId();
    }

    @Override
    public Book findBook(String title) throws CloneNotSupportedException {
        Book tempBook = new Book(title,List.of());
        int index = books.indexOf(tempBook);
        if (index>=0)
            return books.get(index).clone();
        return null;
    }
}
