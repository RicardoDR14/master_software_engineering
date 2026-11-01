public class Ponto<T,T1> {

    T t;
    T1 t1;

    public Ponto(T i, T1 i1) {
        this.t = i;
        this.t1 = i1;
    }

    public void copy(Ponto<? extends T,? extends T1> p) {
        this.t=p.t;
        this.t1=p.t1;
    }

    @Override
    public String toString() {
        return "(" + t + ", " + t1 + ")";
    }
}
