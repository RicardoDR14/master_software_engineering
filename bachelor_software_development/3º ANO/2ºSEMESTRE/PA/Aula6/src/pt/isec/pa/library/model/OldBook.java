package pt.isec.pa.library.model;

import java.util.List;

public class OldBook extends Book {
    private int nCopies;

    public OldBook(String title, List<String> authors, int nCopies) {
        super(title, authors);
        this.nCopies = nCopies;
    }

    public int getnCopies() {
        return nCopies;
    }

    public boolean setnCopies(int nCopies) {
        if (nCopies<1)
            return false;
        this.nCopies = nCopies;
        return true;
    }

    @Override
    public String toString() {
        return " - OldBook - " + super.toString()+" ,#Copies: "+nCopies;
    }
}
