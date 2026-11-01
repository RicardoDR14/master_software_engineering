package pt.isec.pa.library.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Library implements ILibrary {
    protected String name;

    protected Library(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    abstract protected Collection<Book> getBooks();

    abstract protected int addBook(Book book);

    @Override
    public int addOldBook(String title, List<String> authors, int nrCopies) {
        Book newBook = new OldBook(title,authors,nrCopies);
        return addBook(newBook);
    }

    @Override
    public int addRecentBook(String title, List<String> authors, String isbn, double price) {
        Book newBook = new RecentBook(title,authors,isbn,price);
        return addBook(newBook);
    }

    @Override
    public Book findBook(String title) throws CloneNotSupportedException {
        Collection<Book> books = getBooks();
        for(Book book : books)
            if (book.getTitle().equalsIgnoreCase(title))
                return book.clone();
        return null;
    }

    @Override
    public Book findBook(int id) throws CloneNotSupportedException {
        Collection<Book> books = getBooks();
        for(Book book : books)
            if (book.getId()==id)
                return book.clone();
        return null;
    }

    @Override
    public boolean removeBook(String title) {
        Collection<Book> books = getBooks();
        return books.remove(new Book(title,List.of()));
    }

    @Override
    public boolean removeBook(int id) {
        Collection<Book> books = getBooks();
        for(Book book : books)
            if (book.getId()==id)
                return books.remove(book);
        return false;
    }

    @Override
    public String toString() {
        Collection<Book> books = getBooks();
        StringBuilder output = new StringBuilder();
        output.append(String.format("Library %s:\n",name));
        for(Book book : books)
            output.append(String.format("  - %s\n",book));
        return output.toString();
    }
}
