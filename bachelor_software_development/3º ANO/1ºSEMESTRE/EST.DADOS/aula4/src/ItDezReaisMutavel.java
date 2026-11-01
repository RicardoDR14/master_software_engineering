import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ItDezReaisMutavel implements Iterator<Double> {
    int posUltimo = -1;
    boolean podeRemover = false;
    DezReaisMutavel dr;
    int contMod = 0;

    public ItDezReaisMutavel(DezReaisMutavel dr) {
        this.dr = dr;
        contMod = dr.getContModificacoes();
    }

    public void verificaModificacao(){
        if (contMod!= dr.getContModificacoes())throw new ConcurrentModificationException();
    }

    public Double next() {
        verificaModificacao();
        if (posUltimo + 1 >= dr.size()) throw new NoSuchElementException();
        podeRemover = true;
        return dr.get(++posUltimo);
    }

    public boolean hasNext() {
        verificaModificacao();
        return posUltimo + 1 < dr.size();
    }

    public void remove() {
        if (!podeRemover) throw new IllegalStateException();
        podeRemover = false;
        dr.remove(posUltimo);
        posUltimo--;
        contMod++;
    }

    public static void main(String[] args) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) soma++;
            for (int k = 0; k < N; k++) soma++;
        }
    }

}
