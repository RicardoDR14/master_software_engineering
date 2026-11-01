package pt.isec.a2022147797.ex13.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LibrarySet implements ILibrary {

    private String name;
    private Set<Book> books;

    public LibrarySet(String name) {
        this.name = name;
        this.books = new HashSet<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int addBook(String title, List<String> authors) {
        Book newBook = new Book(title, authors);
        if (!books.add(newBook))
            return -1;
        return newBook.getId();
    }

    @Override
    public Book findBook(String title) throws CloneNotSupportedException {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title))
                return book.clone();
        }
        return null;
    }

    @Override
    public Book findBook(int id) throws CloneNotSupportedException {
        for (Book book : books) {
            if (book.getId()==id)
                return book.clone();
        }
        return null;
    }

    @Override
    public boolean removeBook(String title){
        return books.remove(Book.dummyBook(title));
    }

    @Override
    public boolean removeBook(int id){
        for (Book book : books) {
            if (book.getId()==id)
                return books.remove(book);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(String.format("Library Set %s:\n", name));
        for (Book book : books) {
            output.append(String.format(" - %s\n", book));
        }
        return output.toString();
    }
}
