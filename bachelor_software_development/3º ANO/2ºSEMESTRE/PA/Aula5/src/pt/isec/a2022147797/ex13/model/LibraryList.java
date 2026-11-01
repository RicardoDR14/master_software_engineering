package pt.isec.a2022147797.ex13.model;

import java.util.ArrayList;
import java.util.List;

public class LibraryList implements ILibrary {

    private String name;
    private List<Book> books;

    public LibraryList(String name) {
        this.name = name;
        this.books = new ArrayList<>();
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
        if (books.contains(newBook))
            return -1;
        books.add(newBook);
        return newBook.getId();
    }

    @Override
    public Book findBook(String title) throws CloneNotSupportedException {
        Book book = Book.dummyBook(title);
        int index = books.indexOf(book);
        if (index<0)
            return null;
        return books.get(index).clone();
    }

    @Override
    public Book findBook(int id) throws CloneNotSupportedException {
        if (id < 0 || id >= books.size())
            return null;
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
        output.append(String.format("Library List %s:\n", name));
        for (Book book : books) {
            output.append(String.format(" - %s\n", book));
        }
        return output.toString();
    }
}
