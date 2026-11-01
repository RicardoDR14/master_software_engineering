package ficha7.ex1;

import java.util.Comparator;

public class ComparadorComprimento implements Comparator <String>{

    @Override
    public int compare(String o1, String o2) {
        int comprimentoComparacao = Integer.compare(o1.length(), o2.length());

        // Se os comprimentos forem iguais, compara as strings lexicograficamente
        if (comprimentoComparacao == 0) {
            return o1.compareTo(o2);
        }

        return comprimentoComparacao;

    }
}
