package pt.isec.pa.library;

import pt.isec.pa.library.model.ILibrary;
import pt.isec.pa.library.model.LibraryList;
import pt.isec.pa.library.ui.LibraryUI;

public class MainList {
    public static void main(String[] args) throws CloneNotSupportedException {
        ILibrary library = new LibraryList("DEIS-ISEC-List");
        LibraryUI ui = new LibraryUI(library);
        ui.start();
    }
}
