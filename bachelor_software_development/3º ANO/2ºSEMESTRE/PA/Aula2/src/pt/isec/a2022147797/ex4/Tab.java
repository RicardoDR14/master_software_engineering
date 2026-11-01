package pt.isec.a2022147797.ex4;

import java.util.Random;

public class Tab {
    private int[] tab;
    private static final int MAX_ELEMS = 20;
    private int nElems, nTotal;

    public Tab() {
        tab = new int[MAX_ELEMS];
        nElems = 0;
        nTotal = 0;
    }

    public boolean constains(int elem) {
        for (int i = 0; i < nElems; i++) {
            if (tab[i] == elem) return true;
        }
        return false;
    }

    public boolean add(int elem) {
        if (nElems >= MAX_ELEMS) return false;
        nTotal++;
        if (constains(elem)) return false;
        tab[nElems++] = elem;
        return true;
    }

    public void generate() {
        nElems = 0;
        while (nElems < MAX_ELEMS) {
            int elem = new Random().nextInt(101);
            add(elem);
        }
    }

    public void showDuplicateCount(){
        System.out.printf("Duplicados = " + (nTotal-nElems));
    }

    public void show() {
        System.out.print("[");
        for (int i = 0; i < MAX_ELEMS; i++) {
            if (i == 0) System.out.print(i);
            System.out.print(", " + tab[i]);
        }
        System.out.println("]");
    }
}