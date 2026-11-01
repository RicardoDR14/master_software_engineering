package pt.isec.a2022147797.ex13.model;

import java.util.List;

public interface ILibrary {
    String getName();

    void setName(String name);

    int addBook(String title, List<String> authors) throws CloneNotSupportedException;

    Book findBook(String title) throws CloneNotSupportedException;

    Book findBook(int id) throws CloneNotSupportedException;

    boolean removeBook(String title) throws CloneNotSupportedException;

    boolean removeBook(int id) throws CloneNotSupportedException;
}
