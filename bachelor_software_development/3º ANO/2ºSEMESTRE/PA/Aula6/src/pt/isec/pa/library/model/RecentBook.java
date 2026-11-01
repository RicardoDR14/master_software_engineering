package pt.isec.pa.library.model;

import java.util.List;

public class RecentBook extends Book{
    private String isbn;
    private double price;

    public RecentBook(String title, List<String> authors, String isbn, double price) {
        super(title, authors);
        this.isbn = isbn;
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean setIsbn(String isbn) {
        if (isbn==null)
            return false;
        this.isbn = isbn;
        return true;
    }

    public double getPrice() {
        return price;
    }

    public boolean setPrice(double price) {
        if (price<0)
            return false;
        this.price = price;
        return true;
    }

    @Override
    public String toString() {
        return "- Recent Book - "+ super.toString()+",ISBN: "+isbn+", Price: "+price;
    }
}
