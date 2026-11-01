package pt.isec.pa.library;

import pt.isec.pa.library.model.ILibrary;
import pt.isec.pa.library.model.LibrarySet;
import pt.isec.pa.library.ui.LibraryUI;

public class MainSet {
    public static void main(String[] args) throws CloneNotSupportedException {
        ILibrary library = new LibrarySet("DEIS-ISEC-Set");
        LibraryUI ui = new LibraryUI(library);
        ui.start();
    }
}
