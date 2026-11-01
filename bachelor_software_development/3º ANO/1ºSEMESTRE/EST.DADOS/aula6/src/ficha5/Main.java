package ficha5;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Main {

    //FICHA5
    //1.

    public static <T> void preenche(List<? super T> arr, T valor){
        ListIterator i = arr.listIterator();
        while (i.hasNext()){
            i.next();
            i.set(valor);
        }
    }

    //2.
    public static <T> void listaInversa(List<? super T> arr){
        ListIterator i = arr.listIterator(arr.size());
        while (i.hasPrevious()){
            T elemento = (T) i.previous();
            System.out.print(elemento+" ");
        }
        System.out.println("\n");
    }



    ////////////////////////
    ////FUNÇÕES TEMPO

    public static double testa(Pilha p, long sz){
        long startTime=System.nanoTime();
        for (int i = 0; i < sz; i++) {
            p.push(i);
        }
        for (int i = 0; i < sz; i++) {
            p.pop();
        }
        return (System.nanoTime()-startTime)/1000000.0;
    }

    public static double testa(Fila p, long sz){
        long startTime=System.nanoTime();
        for (int i = 0; i < sz; i++) {
            p.add(i);
        }
        for (int i = 0; i < sz; i++) {
            p.remove();
        }
        return (System.nanoTime()-startTime)/1000000.0;
    }


    //ex5
    public static <T> void reverseList1(List<? super T> lista){
        ListIterator iti = lista.listIterator(0);
        ListIterator itf = lista.listIterator(lista.size());
        T temp;
        while (iti.nextIndex() < itf.previousIndex()) {
            temp = (T)iti.next();
            iti.set(itf.previous());
            itf.set(temp);
        }
    }

    //ex6
    public static <T> void imprimeReverse(List<? super T> lista){
        Pilha p = new Pilha<>(lista);
        while(!p.empty()){
            System.out.println(p.peek().toString());
            p.pop();
        }
    }

    //ex7a)
    public static <T> void removePosicoesPares(List<? super T> lista){
        ListIterator it = lista.listIterator();
        while (it.hasNext()){
            it.next();
            if (it.hasNext()){
                it.next();
                it.remove();
            }
        }
    }

    //ex7b)
    public static <T> void removePosicoesPares2(List<? super T> lista){
        List<? super T> aux = new ArrayList<>(lista.size()/2+1);
        ListIterator it = lista.listIterator();

        while (it.hasNext()){
            aux.add((T)it.next());
            it.next();
        }
        lista.clear();
        lista.addAll(aux);
    }



    public static void main(String[] args) {

        /*List<Integer> lista = new ArrayList<>(List.of(1, 2, 3, 4, 5));
       // preenche(lista, 10);
        System.out.println(lista);
        listaInversa(lista);

        List<Character> lista2 = new ArrayList<>(List.of('a','b' , 'c', 'd', 'e'));
       // preenche(lista2, 'f');
        System.out.println(lista2);
        listaInversa(lista2);*/

        /*List alist = new ArrayList<>(); List ilist = new LinkedList();
        long n, sz = 5000*2, nruns=20;
        double tm;
        ficha5.Fila p1 = new ficha5.Fila<>(alist); ficha5.Fila p2 = new ficha5.Fila<>(ilist);
        System.out.println("Tempo de execução médio "+nruns+" execuções (ms)");
        System.out.println("N; ArrayList; LinkedList");
        for (int i = 0; i <= nruns; i++) {
            n = sz * i;
            tm =0;
            for (int j = 0; j < nruns; j++) tm += testa(p1,n);
            System.out.printf("%d; %9.2f; ",i,(float)(tm/nruns));
            tm =0;
            for (int j = 0; j < nruns; j++) tm+=testa(p2,n);
            System.out.printf("%2f\n",(float) (tm/nruns));
        }*/

        List lista = new ArrayList<>();
        lista.add("AA");lista.add("BB");lista.add("CC");lista.add("DD");lista.add("EE");

        /*removePosicoesPares(lista);
        for (Object s : lista) System.out.println(s);*/


    }
}
