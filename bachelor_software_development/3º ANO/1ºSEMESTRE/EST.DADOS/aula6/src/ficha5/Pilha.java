package ficha5;

import java.util.List;
import java.util.ListIterator;

public class Pilha<T>{
    List<? super T> lista;

    public Pilha(List<? super T>l){
        lista = l; lista.clear();
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
