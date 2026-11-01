import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ItDezReaisPositivos implements Iterator<Double> {
    int posUltimo = -1;
    boolean podeRemover = false;
    DezReaisMutavel dr;
    int contMod = 0;

    public int nextPositiveIndex(int from) {
        if (from>=dr.size())return -1;
        while (dr.get(from)<0)
            if (++from>= dr.size())
                return -1;

        return from;
    }

    public ItDezReaisPositivos(DezReaisMutavel dr) {
        this.dr = dr;
        contMod = dr.getContModificacoes();
    }

    public void verificaModificacao() {
        if (contMod != dr.getContModificacoes()) throw new ConcurrentModificationException();
    }

    public Double next() {
        posUltimo=nextPositiveIndex(++posUltimo);
        return dr.get(posUltimo);
    }

    public boolean hasNext() {
        return nextPositiveIndex(posUltimo+1)>=0;
    }

    public void remove() {
        if (!podeRemover) throw new IllegalStateException();
        podeRemover = false;
        dr.remove(posUltimo);
        posUltimo--;
        contMod++;
    }
}
