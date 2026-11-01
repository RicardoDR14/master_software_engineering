import java.util.Iterator;
import java.util.NoSuchElementException;

public class ItDezReais implements Iterator<Double> {
    int posUltimo=-1;
    DezReais dr;

    public ItDezReais(DezReais dr){
        this.dr = dr;
    }

    public Double next() {
        if (posUltimo+1>=dr.size())
            throw new NoSuchElementException();

        return dr.get(++posUltimo);
    }

    public boolean hasNext() {
        return posUltimo+1 < dr.size();
    }
}

