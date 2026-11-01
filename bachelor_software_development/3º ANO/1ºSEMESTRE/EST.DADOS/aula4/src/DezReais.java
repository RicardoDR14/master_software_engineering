
import java.util.Iterator;

public class DezReais implements Iterable<Double>{

    protected final int CAPACITY = 10;
    protected Double [] tab = new Double[CAPACITY];
    protected int last = 0;

    public Double get(int pos){
        return tab[pos];
    }

    public int size(){
        return last;
    }

    public boolean add(Double x){

        if (size()>=CAPACITY)throw new RuntimeException();
        tab[last++]=x;
        return true;
    }

    public Iterator<Double> iterator(){
        return new ItDezReais(this);
    }
}

