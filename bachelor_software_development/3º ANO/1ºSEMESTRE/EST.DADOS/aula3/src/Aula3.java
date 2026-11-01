import java.util.Arrays;


public class Aula3 {

    public static <T> void imprimirArray(T[] array) {
        System.out.println(Arrays.toString(array));  // Converte o array para uma string formatada
    }

    //ex1
    public static <T> boolean elementoRepetido(T[] tab, T o) {
        int count = 0;
        for (T t : tab)
            if (t == o)
                count++;

        return count > 1;
    }

    //ex2
    public static <T extends Comparable<T>> boolean elementoRepetido2(T[] tab, T o) {
        int count = 0;
        for (T t : tab)
            if (t.compareTo(o) == 0)
                count++;

        return count > 1;
    }

    //ex3c)
    public static <T extends Comparable<? super T>> int compararObjetos(T objeto1, T objeto2) {
        return objeto1.compareTo(objeto2);
    }

    //ex4
    public static <T extends Comparable<T>> boolean search(T [] tab, T elemento){

        for (T t: tab )
            if (compararObjetos(elemento,t)<0)
                return true;

        return false;
    }


    public static void main(String[] args) {

        /*//ex1 e ex2
        String[] arrayDeStrings = {"a", "b", "a", "c", "a"};
        String elemento1 = arrayDeStrings[0]; // Comparando por referência
        System.out.println(elementoRepetido2(arrayDeStrings, elemento1)); // Deve retornar true

        // Exemplo com Integers
        Integer[] arrayDeIntegers = {1, 2, 3, 2, 4, 5};
        Integer elemento2 = arrayDeIntegers[1];
        System.out.println(elementoRepetido2(arrayDeIntegers, elemento2)); // Deve retornar true

        // Exemplo onde não há repetição
        String elemento3 = new String("d"); // Novo objeto, não existe no array
        System.out.println(elementoRepetido2(arrayDeStrings, elemento3)); // Deve retornar false

        // Exemplo com Float
        Float[] arrayDeFloats = {1.1f, 2.2f, 3.3f, 2.2f, 4.4f, 5.5f};
        Float elementoFloat = arrayDeFloats[1];  // Comparando o segundo elemento (2.2f)
        System.out.println(elementoRepetido2(arrayDeFloats, elementoFloat));  // Deve retornar true

        // Exemplo com Double
        Double[] arrayDeDoubles = {1.1, 2.2, 3.3, 4.4, 5.5};
        Double elementoDouble = arrayDeDoubles[1];  // Comparando o segundo elemento (2.2)
        System.out.println(elementoRepetido2(arrayDeDoubles, elementoDouble));  // Deve retornar false (não há repetição)

        // Exemplo onde há repetição
        Double[] arrayDeDoublesRepetido = {1.1, 2.2, 3.3, 2.2, 5.5};
        Double elementoRepetido = arrayDeDoublesRepetido[1];  // Comparando o segundo elemento (2.2)
        System.out.println(elementoRepetido2(arrayDeDoublesRepetido, elementoRepetido));  // Deve retornar true*/

        /*//ex3a) e b)
        // Testando comparação entre Rectangulo e outra Figura (Rectangulo)
        Retangulo r1 = new Retangulo(4, 5);  // Área 20
        Retangulo r2 = new Retangulo(3, 7);  // Área 21
        System.out.println(Retangulo.compararRetObj(r1, r2));  // Deve retornar um valor negativo (r1 < r2)

        // Exemplo com uma classe X que implementa Comparable<Rectangulo>
        X objX = new X(15);  // Comparável pela área
        System.out.println(Retangulo.compararRetObj(r1, objX));  // Comparando com objeto X

        // Exemplo com uma classe Z que implementa Comparable<Object>
        Z objZ = new Z();
        System.out.println(Retangulo.compararRetObj(r1, objZ));  // Comparando com objeto Z*/

        /*//ex3c)
        // Comparando dois Rectangulos
        Retangulo r1 = new Retangulo(3, 4);
        Retangulo r2 = new Retangulo(5, 2);
        System.out.println(compararObjetos(r1, r2));  // Deve retornar a comparação entre as áreas

        // Comparando dois Integers
        Integer num1 = 10;
        Integer num2 = 20;
        System.out.println(compararObjetos(num1, num2));  // Deve retornar a comparação entre os números*/

        /*//ex4
        Integer m[]={3,2,6,3};
        String n[]={"Ada", "Albino"};
        System.out.println(search(m,2)); // true
        System.out.println(search(n,"Francisco")); // false*/

        //ex5
        Ponto<Integer, Integer> p=new Ponto< >(3,4);
        Ponto<Number, Number> x=new Ponto<>(0,0);
        System.out.println(p); // imprime (3,4)
        System.out.println(x); // imprime (0,0)
        x.copy(p);
        System.out.println(x); // imprime (3,4)
        Ponto<String, Integer> erro= new Ponto<String,Integer>("olá",3);
        System.out.println(erro);
    }
}


