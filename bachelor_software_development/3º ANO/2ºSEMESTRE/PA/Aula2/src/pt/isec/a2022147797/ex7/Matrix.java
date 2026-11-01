package pt.isec.a2022147797.ex7;

import java.util.Arrays;
import java.util.Random;

public class Matrix {
    private float m[][];

    public Matrix(int r, int c) {
        m = new float[r][c];
    }

    public Matrix(Matrix other) {
        //m = new float[other.m.length][other.m[0].length];

        this(other.m.length,other.m[0].length);

        /*for (int r = 0; r < m.length; r++) {
            for (int c = 0; c < m[0].length; c++) {
                m[r][c] = other.m[r][c];
            }
        }*/

        /*for (int r = 0; r < other.m.length; r++) {
            System.arraycopy(
                    other.m[r],0,
                    m[r],0,
                    other.m[r].length
            );
        }*/

        m = new float[other.m.length][];
        for (int r = 0; r < other.m.length; r++) {
            m[r]= Arrays.copyOf(other.m[r], other.m[r].length);
        }
    }

    public void generate() {
        for (int i = 0; i < m.length; i++)
            for (int c = 0; c < m[i].length; c++)
                m[i][c] = (float) (Math.random()*100);
    }

    public void show() {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++)
                System.out.printf("%10.5f ", m[i][j]);
            System.out.println();
        }
    }

    public float get(int r, int c) {
        return m[r][c];
    }

    public boolean set(int r, int c, float value) {
        if (r < 0 || c < 0 || r < m.length && c < m[0].length)
            return false;
        m[r][c] = value;
        return true;
    }

    public boolean add(Matrix other) {
        if (m.length != other.m.length || m[0].length != other.m[0].length)
            return false;

        for (int r = 0; r< m.length; r++)
            for (int c = 0; c < m[r].length; c++)
                m[r][c] += other.m[r][c];

        return true;
    }

    public static Matrix add(Matrix m1, Matrix m2){
        Matrix newMatrix = new Matrix(m1);
        if (!newMatrix.add(m2))
            return null;
        return newMatrix;
    }
}
