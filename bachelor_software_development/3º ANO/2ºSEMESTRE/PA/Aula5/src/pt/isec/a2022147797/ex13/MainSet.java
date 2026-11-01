package pt.isec.a2022147797.ex13;

import pt.isec.a2022147797.ex13.model.ILibrary;
import pt.isec.a2022147797.ex13.model.LibraryList;
import pt.isec.a2022147797.ex13.model.LibrarySet;
import pt.isec.a2022147797.ex13.ui.LibraryUI;

public class MainSet {
    public static void main(String[] args) throws CloneNotSupportedException {
        ILibrary library = new LibrarySet("My Library List");
        LibraryUI ui = new LibraryUI(library);
        ui.start();
    }
}
