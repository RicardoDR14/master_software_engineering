package pt.isec.a2022147797.ex13.ui;

import pt.isec.a2022147797.ex13.model.Book;
import pt.isec.a2022147797.ex13.model.ILibrary;
import pt.isec.a2022147797.ex13.ui.utils.PAInput;

import java.util.ArrayList;

public class LibraryUI {
    ILibrary lib;
    public LibraryUI(ILibrary lib) {
        this.lib = lib;
    }

    void addBook() throws CloneNotSupportedException {
        String title = PAInput.readString("Book title: ",false);
        String author;
        ArrayList<String> authors = new ArrayList<>();
        do {
            author = PAInput.readString("Name of one author [\'exit\' to finish]: ",false);
            if (author!=null && !author.equalsIgnoreCase("exit"))
                authors.add(author);
        } while (!author.equalsIgnoreCase("exit"));
        if (authors.isEmpty())
            authors.add("Author unknown");
        int id = lib.addBook(title,authors);
        if (id<0)
            System.out.println("Error adding this new book");
        else
            System.out.printf("The ID of this new book is: %d\n",id);
    }

    void findBook() throws CloneNotSupportedException {
        String title = PAInput.readString("Title of the book to search: ",false);
        Book book = lib.findBook(title);
        if (book == null)
            System.out.println("Book not found");
        else
            System.out.println("Book found: "+book);
    }

    void findBookById() throws CloneNotSupportedException {
        int id = PAInput.readInt("Id of the book to search: ");
        Book book = lib.findBook(id);
        if (book == null)
            System.out.println("Book not found");
        else
            System.out.println("Book found: "+book);
    }

    void removeBook() throws CloneNotSupportedException {
        String title = PAInput.readString("Title of the book to remove: ",false);
        boolean deleted = lib.removeBook(title);
        if (!deleted)
            System.out.println("Book not found");
        else
            System.out.println("Book deleted");
    }

    void removeBookById() throws CloneNotSupportedException {
        int id = PAInput.readInt("Id of the book to remove: ");
        boolean deleted = lib.removeBook(id);
        if (!deleted)
            System.out.println("Book not found");
        else
            System.out.println("Book deleted");
    }
    public void start() throws CloneNotSupportedException {
        while (true) {
            switch (PAInput.chooseOption("LibraryList Manager - "+lib.getName(),
                    "Add new book",
                    "Search book (by title)", "Search book (by id)",
                    "Remove book (by title)", "Remove book (by id)",
                    "Show books", "Quit"
            )) {
                case 1:
                    addBook();
                    break;
                case 2:
                    findBook();
                    break;
                case 3:
                    findBookById();
                    break;
                case 4:
                    removeBook();
                    break;
                case 5:
                    removeBookById();
                    break;
                case 6:
                    System.out.println(lib.toString());
                    break;
                case 7:
                    return;
            }
        }
    }

}
