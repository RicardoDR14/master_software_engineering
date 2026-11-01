public class Retangulo extends Figura{
    private int largura = 0, altura = 0;

    public Retangulo(int l, int a) {
        this.largura=l;
        this.altura=a;
    }

    @Override
    public double calcularArea() {
        return largura * altura;
    }

    public static <T extends Comparable<? super Retangulo>> int compararRetObj(Retangulo r, T outro){
        if (outro instanceof Figura) {
            Figura outraFigura = (Figura) outro;
            return r.compareTo(outraFigura); // Comparação entre retângulo e figura
        } else if (outro instanceof Comparable) {
            // Supondo que o objeto seja comparável a Rectangulo
            return outro.compareTo(r); // Comparação reversa, depende de outroObjeto
        } else {
            throw new IllegalArgumentException("O objeto fornecido não é comparável a Rectangulo");
        }
    }
}

class Z implements Comparable<Object> {
    @Override
    public int compareTo(Object o) {
        if (o instanceof Retangulo) {
            Retangulo r = (Retangulo) o;
            return 0;  // Exemplo trivial
        }
        return -1;
    }
}

class X implements Comparable<Retangulo> {
    private double area;

    public X(double area) {
        this.area = area;
    }

    @Override
    public int compareTo(Retangulo r) {
        return Double.compare(this.area, r.calcularArea());
    }
}
