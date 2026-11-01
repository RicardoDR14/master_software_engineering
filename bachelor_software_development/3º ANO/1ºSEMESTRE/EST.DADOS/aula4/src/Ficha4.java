import java.util.ArrayList;
import java.util.Iterator;

public class Ficha4 {

    public static <T extends Comparable<? super T>> T procuraMaior(Iterable<T> d) {
        Iterator<T> iterator = d.iterator();

        T maior = iterator.next();

        while (iterator.hasNext()) {
            T atual = iterator.next();
            if (atual.compareTo(maior)>0) {
                maior = atual;
            }
        }
        return maior;
    }

    public static void main(String[] args) {

        // Teste com ArrayList
        ArrayList<Double> lista = new ArrayList<>();
        lista.add(1.5);
        lista.add(2.3);
        lista.add(0.9);
        lista.add(5.7);

        System.out.println("Maior número na ArrayList: " + procuraMaior(lista));

        // Teste com DezReaisMutavel
        DezReaisMutavel dezReaisMutavel = new DezReaisMutavel();
        dezReaisMutavel.add(1.1);
        dezReaisMutavel.add(3.9);
        dezReaisMutavel.add(2.7);
        dezReaisMutavel.add(6.5);

        System.out.println("Maior número em DezReaisMutavel: " + procuraMaior(dezReaisMutavel));

        // Teste com DezReaisMutavel
        DezReais dezReais = new DezReais();
        dezReais.add(3.14);
        dezReais.add(7.89);
        dezReais.add(12.56);
        dezReais.add(0.99);

        System.out.println("Maior número em DezReaisMutavel: " + procuraMaior(dezReais));
    }


}


