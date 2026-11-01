package ficha5;

import ficha5.Pilha;

import java.util.ArrayList;

public class Pilha2 <T extends Comparable<? super T>>{
    Pilha minimos = new Pilha<>(new ArrayList<>());
    Pilha principal = new Pilha<>(new ArrayList<>());

    public void pop(){
        T ret = (T)principal.peek();
        if (ret.compareTo((T)minimos.peek())==0)
            minimos.pop();
        principal.pop();
    }

    public void push(T valor){
        if (valor.compareTo((T)minimos.peek()) <=0 || minimos.empty())
            minimos.push(valor);
        principal.push(valor);
    }

    public T findMin(){
        return (T)minimos.peek();
    }
}
