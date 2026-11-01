import java.util.List;
import java.util.ListIterator;

public class Fila <T>{

    List<? super T> lista;

    public Fila(List<? super T>l){
        lista = l; l.clear();
    }

    public boolean empty(){
        return lista.size()==0;
    }

    public void add(T n){
        lista.add(n);
    }

    public T remove(){
        return (T)lista.remove(0);
    }
    public T element(){
        ListIterator <? super T> li = lista.listIterator();
        return (T) li.next();
    }

}
