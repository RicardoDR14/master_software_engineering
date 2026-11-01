import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PilhaValoresGenericos <T>{
    List<? super T> lista;

    public PilhaValoresGenericos(List<? super T>l){
        lista = l; l.clear();
    }

    public boolean empty(){
        return lista.size()==0;
    }

    public T peek(){
        ListIterator iterator = lista.listIterator(lista.size());
        return (T) iterator.previous();
    }

    public T pop(){
        return (T) lista.remove(lista.size()-1);
    }

    public void push(T valor){
        lista.add(valor);
    }

}
