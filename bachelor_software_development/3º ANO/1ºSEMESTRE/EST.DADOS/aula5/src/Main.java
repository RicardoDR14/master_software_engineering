import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Main {

    //FICHA1
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

    public static double testa(PilhaValoresGenericos p, long sz){
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




    public static void main(String[] args) {

        /*List<Integer> lista = new ArrayList<>(List.of(1, 2, 3, 4, 5));
       // preenche(lista, 10);
        System.out.println(lista);
        listaInversa(lista);

        List<Character> lista2 = new ArrayList<>(List.of('a','b' , 'c', 'd', 'e'));
       // preenche(lista2, 'f');
        System.out.println(lista2);
        listaInversa(lista2);*/

        List alist = new ArrayList<>(); List ilist = new LinkedList();
        long n, sz = 5000*2, nruns=20;
        double tm;
        Fila p1 = new Fila<>(alist); Fila p2 = new Fila<>(ilist);
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
        }


    }
}
