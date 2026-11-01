package pt.isec.pa.library;

import pt.isec.pa.library.model.ILibrary;
import pt.isec.pa.library.model.LibraryMap;
import pt.isec.pa.library.ui.LibraryUI;

public class MainMap {
    public static void main(String[] args) throws CloneNotSupportedException {
        ILibrary library = new LibraryMap("DEIS-ISEC-Map");
        LibraryUI ui = new LibraryUI(library);
        ui.start();
    }
}