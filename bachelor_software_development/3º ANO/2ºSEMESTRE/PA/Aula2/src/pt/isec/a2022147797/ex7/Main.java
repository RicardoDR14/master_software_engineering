package pt.isec.a2022147797.ex7;

public class Main {
    public static void main(String[] args) {
        Matrix m1 = new Matrix(4, 3);
        m1.generate();
        m1.show();
        System.out.println("-".repeat(50));
        Matrix m2 = new Matrix(m1);
        float oldValue = m2.get(1, 1);
        m2.set(1, 1, (float) Math.PI);
        System.out.printf("%.2f => %.2f%n", oldValue, m2.get(1, 1));
        System.out.print(System.lineSeparator());
        m2.show();
        System.out.println("-".repeat(50));
        m1.add(m2, m2);
        m1.show();
        System.out.println("-".repeat(50));
        Matrix m3 = Matrix.add(m1, m2);
        if (m3 != null)
            m3.show();
        else
            System.out.println("Error");
    }
}