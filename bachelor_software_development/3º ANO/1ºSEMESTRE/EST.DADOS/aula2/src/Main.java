import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.abs;

public class Main {
    static int[] criaArrayCom(
            int valor,
            int dimensao,
            boolean diferentes) {
        int m[] = new int[dimensao];
        if (diferentes) {
            for (int i = 0; i < dimensao; i++)
                m[i] = i * 10;
            if ((valor % 10 != 0) || (0 > valor) || (valor > (dimensao - 1) * 10))
                m[0] = valor;
        } else {
            Random r = new Random();
            int gama = (abs(valor) < 10) ? 10 : abs(valor);
            for (int i = 0; i < dimensao; i++)
                m[i] = r.nextInt(gama * 4) - gama * 2;
            m[0] = valor;
        }
        Arrays.sort(m);
        return m;
    }

    public static boolean binRecursivo(int[] tab, int chave) {

        int tam = tab.length;
        int[] newt;

        if (tam == 0)
            return false;

        if (tab[tam / 2] == chave)
            return true;
        else if (tab[tam / 2] < chave)
            newt = Arrays.copyOfRange(tab, tam / 2, tam - 1);
        else
            newt = Arrays.copyOfRange(tab, 0, tam / 2);

        return binRecursivo(newt, chave);
    }

    public static boolean binIterativa(int[] tab, int chave) {
        int start = 0;
        int end = tab.length - 1;
        int meio = (start + end) / 2;

        while (start <= end) {
            if (tab[meio] == chave) return true;
            else if (tab[meio] < chave) {
                start = meio + 1;
            } else if (tab[meio] > chave) {
                end = meio - 1;
            }
            meio = (start + end) / 2;
        }
        return false;
    }

    public static int binPosicao(int[] tab, int chave) {
        int start = 0;
        int end = tab.length - 1;
        int meio;

        while (start <= end) {
            meio = (start + end) / 2;
            if (tab[meio] == chave) return meio;
            else if (tab[meio] < chave) {
                start = meio + 1;
            } else {
                end = meio - 1;
            }
        }
        return -1;
    }

    public static int binPosicao2(int[] tab, int chave) {
        int start = 0;
        int end = tab.length - 1;
        int meio = 0;

        while (start <= end) {
            meio = (start + end) / 2;
            if (tab[meio] == chave) return meio;
            else if (tab[meio] < chave) {
                start = meio + 1;
            } else {
                end = meio - 1;
            }
        }
        if (tab[meio] < chave) return -meio - 2;
        return -meio - 1;
    }

    public static double binPosicao3(int[] tab, int chave) {
        int pos = binPosicao2(tab, chave);

        if (pos >= 0) return (double) pos / tab.length;

        int posInsert = pos - 1;
        return (double) posInsert / tab.length;
    }

    public static double binPosicao4(int[] tab, int chave1, int chave2) {
        int j, k, count = 0;

        /*for (int j = 0; j < tab.length && tab[j]<=chave2; j++) {
            if (tab[j] >= chave1 && tab[j]<chave2){
                count++;
            }
        }*/

        j = binPosicao2(tab, chave1);
        k = binPosicao2(tab, chave2);
        if (j < 0) j = -j - 1;
        if (k < 0) k = -k - 1;
        else k++;
        return k - j;
    }

    public static boolean binPosicao5(int[] tab, int chave) {
        int j;

        j = binPosicao2(tab, chave);
        if (j < 0) return false;

        if (j == 0)
            if (tab[j] == tab[j + 1])
                return true;

        if (j == tab.length)
            if (tab[j] == tab[j - 1])
                return true;

        if (tab[j] == tab[j + 1] || tab[j] == tab[j - 1]) return true;

        return false;
    }

    public static int binPosicao6(int[] tab, int chave) {
        int j;

        j = binPosicao2(tab, chave);

        if (j == 0 || j == -1)
            return chave;

        if (j < 0) j = -j - 1;

        return tab[j - 1];

    }

    public static int binPosicao7(int[] tab, int chave) {

        int start = 0;
        int end = tab.length - 1;
        int meio;

        while (start <= end) {
            meio = (start + end) / 2;
            if (tab[meio] == chave)
                return meio;
            if (tab[meio] * chave >= 0) {
                if (tab[meio] < chave)
                    start = meio + 1;
                else
                    end = meio - 1;
            } else if (tab[meio] < chave)
                end = meio - 1;
            else
                start = meio + 1;
        }
        return -1;
    }

    public static int binPosicao8(int[] tab) {
            int start = 0;
            int end = tab.length - 1;
            int result = -1;

            while (start <= end) {
                int mid = (start + end) / 2;

                if (tab[mid] > mid) {
                    result = mid;
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            }

            return result;
        }

    public static void main(String[] args) {
        //int [] array = criaArrayCom(20,5,true);
        int[] array = {-3,1, 7, 12, 15};
        for (int a : array) System.out.println(a);


        /* 1. boolean res = binRecursivo(array,10);*/
        /* 2. boolean res = binIterativa(array,10);
        *
        * if (res==true) System.out.println("Existe");
        else System.out.println("Não existe");*/

        /* 3. int res = binPosicao(array,15);*/
        /* 4. int res = binPosicao2(array,50);*/
        /* 5. double res = binPosicao3(array,10);*/
        /* 6. double res = binPosicao4(array,10,20);*/
        /* 7. boolean res = binPosicao5(array,10);*/
        /* 8. int res = binPosicao6(array,15);*/

        int res = binPosicao8(array);
        System.out.println("resultado: " + res);
    }
}
