package ficha7.ex1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        BinaryTree<Integer> arvore = new BinaryTree<>();
        arvore.insere(10);
        arvore.insere(5);
        arvore.insere(15);
        arvore.insere(3);
        arvore.insere(7);
        arvore.insere(20);


        System.out.println("Árvore contém 7? " + arvore.contem(7));
        System.out.println("Profundidade de 7: " + arvore.profundidadeDe(7));
        System.out.println("Tamanho da árvore: " + arvore.tamanho());
        System.out.println("Profundidade da árvore: " + arvore.profundidade());

        System.out.print("Impressão em ordem: ");
        arvore.imprimeOrdem();

        System.out.print("Impressão por níveis: ");
        arvore.imprimeNíveis();

        System.out.println("Árvore binária desenhada:");
        arvore.imprimeArvore();  // Impressão visual da árvore


        //ex2
        BinaryTree <String> arvore2 = new BinaryTree<>();
        arvore2.insere("D");
        arvore2.insere("B");
        arvore2.insere("A");
        arvore2.insere("C");
        arvore2.insere("E");
        arvore2.insere("F");
        arvore2.insere("H");
        arvore2.insere("G");

        arvore2.imprimeArvore();

        //ex3
        arvore2.imprimeNíveis();

        //ex4
        ComparadorComprimento comprimentoComparator = new ComparadorComprimento();

        // Criando a árvore binária com o comparador
        BinaryTree<String> arvore4 = new BinaryTree<>(comprimentoComparator);

        // Inserindo algumas strings na árvore
        arvore4.insere("Abacaxi");
        arvore4.insere("Maçã");
        arvore4.insere("Banana");
        arvore4.insere("Pera");
        arvore4.insere("Uva");

        // Imprimindo a árvore em ordem (para verificar a ordenação por comprimento)
        System.out.println("Árvore em ordem (ordenada por comprimento das strings):");
        arvore4.imprimeOrdem();

        //ex5
        arvore4.imprimeOrdem();

        System.out.println();
        System.out.println("ex6");
        //ex6
        // Gerar números de 1 a 1.000.000
        int N = 1000000;
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            numbers.add(i);
        }

        // Baralhar a lista de números
        Collections.shuffle(numbers);

        // Criar a árvore binária
        BinaryTree<Integer> arvore6= new BinaryTree<>();

        // Inserir números na árvore
        for (int num : numbers) {
            arvore6.insere(num);
        }

        // Calcular e imprimir a profundidade da árvore
        int profundidadeReal = arvore6.profundidade();
        System.out.println("Profundidade da árvore: " + profundidadeReal);

        // Calcular a profundidade teórica (ideal)
        double profundidadeIdeal = Math.log(N) / Math.log(2);
        System.out.println("Profundidade ideal (teórica): " + profundidadeIdeal);

        // Calcular e imprimir o desvio
        double desvio = profundidadeReal - profundidadeIdeal;
        System.out.println("Desvio em relação ao valor teórico: " + desvio);

        /*
        System.out.println();
        System.out.println("ex7");
        BinaryTree<Integer> arvore7 = new BinaryTree<>();

        // Inserindo alguns elementos na árvore
        arvore7.insere(50);
        arvore7.insere(30);
        arvore7.insere(70);
        arvore7.insere(20);
        arvore7.insere(40);
        arvore7.insere(60);
        arvore7.insere(80);

        System.out.println("Árvore original:");
        arvore7.imprimeOrdem();

        // Removendo um nó (por exemplo, 30)
        arvore7.remove(30);
        System.out.println("Árvore após remover 30:");
        arvore7.imprimeOrdem();

        // Removendo um nó com dois filhos (por exemplo, 50)
        arvore7.remove(50);
        System.out.println("Árvore após remover 50:");
        arvore7.imprimeOrdem();

        // Removendo um nó sem filhos (por exemplo, 20)
        arvore7.remove(20);
        System.out.println("Árvore após remover 20:");
        arvore7.imprimeOrdem();
        // Tentando remover um valor null (isso irá gerar uma exceção)
        try {
            arvore7.remove(null);  // Tentando remover um valor null
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao remover valor nulo: " + e.getMessage());
        }*/

        System.out.println();
        System.out.println("ex8");
        //ex8
        BinaryTree<Integer> arvore8 = new BinaryTree<>();

        // Inserindo alguns elementos na árvore
        arvore8.insere(50);
        arvore8.insere(30);
        arvore8.insere(70);
        arvore8.insere(20);
        arvore8.insere(40);
        arvore8.insere(60);
        arvore8.insere(80);

        System.out.println("Árvore original:");
        arvore8.imprimeArvore();

        //remover folhas
        arvore8.removeFolhas();
        System.out.println("Árvore após remover folhas:");
        arvore8.imprimeArvore();

        //ex9
        System.out.println();
        System.out.println("ex9");
        BinaryTree<Integer> arvore9 = new BinaryTree<>();

        // Inserindo alguns elementos na árvore
        arvore9.insere(50);
        arvore9.insere(30);
        arvore9.insere(70);
        arvore9.insere(20);
        arvore9.insere(40);
        arvore9.insere(60);
        arvore9.insere(80);

        System.out.println("Árvore original:");
        arvore9.imprimeArvore();

        //procurar sucessor
        System.out.println();
        Node1 sucessor= arvore9.encontrarSucessor(35);
        System.out.println("o sucessor de 35 é: " + sucessor.get());

        //ex10
        System.out.println();
        System.out.println("ex10");
        BinaryTree<Integer> arvore10 = new BinaryTree<>();

        // Inserindo alguns elementos na árvore
        arvore10.insere(50);
        arvore10.insere(30);
        arvore10.insere(70);
        arvore10.insere(20);
        arvore10.insere(40);
        arvore10.insere(60);
        arvore10.insere(80);

        System.out.println("Árvore 10:");
        arvore10.imprimeArvore();

        //verificar se é igual
        System.out.println();
        BinaryTree<Integer> arvore11 = new BinaryTree<>(); // Cria uma árvore igual a arvore10>

        arvore11.insere(50);
        arvore11.insere(30);
        arvore11.insere(70);
        arvore11.insere(20);
        arvore11.insere(40);
        arvore11.insere(60);
        arvore11.insere(80);

        System.out.println("Árvore 11:");
        arvore11.imprimeArvore();

        System.out.println();
        System.out.println("As árvores 10 e 11 são iguais: " + arvore10.isArvoreIgual(arvore11));

        //ex11
        System.out.println();
        System.out.println("ex11");
        BinaryTree<Integer> arvore12 = new BinaryTree<>();

        // Inserindo alguns elementos na árvore
        arvore12.insere(50);
        arvore12.insere(30);
        arvore12.insere(70);
        arvore12.insere(20);
        arvore12.insere(40);
        arvore12.insere(60);
        arvore12.insere(80);

        System.out.println("Árvore 12:");
        arvore12.imprimeArvore();

        arvore12.removeFolhas();

        //percorrer em ordem
        System.out.println();
        System.out.println("Percorrer em ordem:");
        List<Integer> ordem = arvore12.percorrerEmOrdem();
        System.out.println(ordem);



    }
}