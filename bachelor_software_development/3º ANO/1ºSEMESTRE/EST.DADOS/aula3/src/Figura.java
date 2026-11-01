public abstract class Figura implements Comparable<Figura> {

    public abstract double calcularArea();

    @Override
    public int compareTo(Figura o) {
        return Double.compare(this.calcularArea(),o.calcularArea());
    }
}

