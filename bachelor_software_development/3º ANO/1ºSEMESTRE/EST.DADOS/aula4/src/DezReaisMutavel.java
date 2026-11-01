import java.util.Iterator;

public class DezReaisMutavel extends DezReais {

     private int contModificacoes = 0;

    public void acrescenta(Double valor){
        contModificacoes++;
        add(valor);
    }

    void remove(int pos) {
        int j;
        for (j = 0; j < last; j++) {
            tab[j] = tab[j + 1];
        }
        if (last > -1) last--;
        contModificacoes++;
    }

    public int getContModificacoes(){
        return contModificacoes;
    }


    public Iterator<Double> iterator() {
        return new ItDezReaisMutavel(this);
    }

}
