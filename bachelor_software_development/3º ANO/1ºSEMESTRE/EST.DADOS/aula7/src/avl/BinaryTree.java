package avl;

import java.util.*;


public class BinaryTree<T extends Comparable<T>> {
    private Node<T> root;
    private Comparator<T> comparator;
    private int size;

    public BinaryTree() {
        this.comparator = null;
        this.size = 0;
        this.root = (Node<T>) Node.SENTINELA;  //ex5
    }

    public BinaryTree(Comparator<T> comparator) {
        this.comparator = comparator;
        this.size = 0;
        this.root = (Node<T>) Node.SENTINELA;  //ex5
    }

    private int compare(T a, T b) {
        if (a == null && b == null) {
            return 0; // Considera nulos como iguais
        }
        if (a == null) {
            return -1; // Nulo é considerado menor
        }
        if (b == null) {
            return 1; // Nulo é considerado menor que qualquer valor não nulo

        }
        // Caso ambos sejam não nulos, utiliza o Comparator, ou Comparable
        if (comparator == null) {
            return a.compareTo(b);
        }
        return comparator.compare(a, b);
    }


    public void insere(T t) {
        Node n = new Node(t);
        root = insere(root, n);
    }

    private Node<T> insere(Node<T> root, Node<T> newNode) {
        //if (root == null) {
        //    size++;  // Incrementa o tamanho quando um novo nó é inserido
        //     return newNode;
        //}

        //ex5
        if (root.isSentinela()) {
            size++;
            return newNode;
        }
        //

        int cmp;
        if (comparator == null) {
            cmp = ((Comparable<T>) root.get()).compareTo(newNode.get());  // Usando compareTo se não houver comparador
        } else {
            cmp = comparator.compare(root.get(), newNode.get());  // Usando o comparador se ele existir
        }

        if (cmp == 0) {
            throw new RuntimeException("Valor duplicado");  // Valor duplicado
        }
        if (cmp > 0) {
            root.setLeft(insere(root.getLeft(), newNode));  // Vai para a subárvore à esquerda
        } else {
            root.setRight(insere(root.getRight(), newNode));  // Vai para a subárvore à direita
        }

        return root;
    }

    public boolean contem(T t) {
        return contem(root, t);
    }

    private boolean contem(Node<T> current, T value) {
        if (current == null) {
            return false;
        }
        int cmp = compare(value, current.get());
        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return contem(current.getLeft(), value);
        } else {
            return contem(current.getRight(), value);
        }
    }


    // Método para encontrar a profundidade de um elemento
    public int profundidadeDe(T value) {
        return profundidade(root, value, 1);
    }

    private int profundidade(Node<T> current, T value, int depth) {
        if (current == null) {
            return 0;
        }
        int cmp = compare(value, current.get());
        if (cmp == 0) {
            return depth;
        } else if (cmp < 0) {
            return profundidade(current.getLeft(), value, depth + 1);
        } else {
            return profundidade(current.getRight(), value, depth + 1);
        }
    }

    public void imprimeOrdem() {
        imprimeOrdem(root);
        System.out.println();
    }

    private void imprimeOrdem(Node<T> current) {
        // if (current != null) {
        //     imprimeOrdem(current.getLeft());
        //     System.out.print(current.get() + " ");
        //     imprimeOrdem(current.getRight());

        //ex5
        if (current.isSentinela()) {
            return;  // Não imprime o nó sentinela
        }
        imprimeOrdem(current.getLeft());
        System.out.print(current.get() + " ");
        imprimeOrdem(current.getRight());

    }

    public int profundidade() {
        return profundidade(root);
    }

    private int profundidade(Node<T> current) {
        if (current == null) {
            return 0;
        }
        int leftProfundidade = profundidade(current.getLeft());
        int righProfundidade = profundidade(current.getRight());
        return Math.max(leftProfundidade, righProfundidade) + 1;

    }

    public int tamanho() {
        return size;
    }

    // Método para imprimir os níveis da árvore
    public void imprimeNíveis() {
        if (root == null) {
            return;
        }
        Queue<Node<T>> fila = new LinkedList<>();
        fila.add(root);
        while (!fila.isEmpty()) {
            Node<T> current = fila.poll();
            System.out.print(current.get() + " ");
            if (current.getLeft() != null) {
                fila.add(current.getLeft());
            }
            if (current.getRight() != null) {
                fila.add(current.getRight());
            }
        }
        System.out.println();
    }

    public void imprimeArvore() {
        imprimeArvore(root, "", true);
    }

    private void imprimeArvore(Node<T> node, String indent, boolean last) {
        if (node != null) {
            System.out.println(indent + (last ? "└── " : "├── ") + node.get());
            indent += last ? "    " : "│   ";
            imprimeArvore(node.getLeft(), indent, false);
            imprimeArvore(node.getRight(), indent, true);
        }
    }

    public void remove(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot remove null value");
        }
        root = remove(root, value);
    }

    private Node<T> remove(Node<T> current, T value) {
        if (current == null) {
            return null;  // Elemento não encontrado
        }

        // Compara o valor para encontrar o nó a ser removido
        if (value.compareTo(current.get()) < 0) {
            current.setLeft(remove(current.getLeft(), value));
        } else if (value.compareTo(current.get()) > 0) {
            current.setRight(remove(current.getRight(), value));
        } else {
            // Encontrou o nó a ser removido

            // Caso 1: Nó sem filhos (folha)
            if (current.getLeft() == null && current.getRight() == null) {
                size--;
                return null;
            }

            // Caso 2: Nó com um único filho
            if (current.getLeft() == null) {
                size--;
                return current.getRight();
            } else if (current.getRight() == null) {
                size--;
                return current.getLeft();
            }

            // Caso 3: Nó com dois filhos
            // Encontrar o sucessor (o menor valor na subárvore direita)
            Node<T> successor = findMin(current.getRight());
            // Substitui o valor do nó atual pelo sucessor
            current.setRight(remove(current.getRight(), successor.get()));
            current.setLeft(successor.getLeft());
            current.setRight(successor.getRight());
        }
        return current;
    }

    private Node<T> findMin(Node<T> current) {
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    public void removeFolhas() {
        root = removeFolhas(root);
    }

    private Node<T> removeFolhas(Node<T> current) {
        if (current == null) {
            return null;
        }

        if (current.getLeft() == null && current.getRight() == null) {
            size--;
            return null;
        }
        current.setLeft(removeFolhas(current.getLeft()));
        current.setRight(removeFolhas(current.getRight()));
        return current;
    }

    public Node<T> encontrarSucessor(T value) {
        Node<T> target = new Node<>(value);
        return encontrarSucessor(root, target);
    }

    private Node<T> encontrarSucessor(Node<T> root, Node<T> target) {
        if (root == null || target == null) {
            return null;
        }

        Node successor = null;

        while (root != null) {
            //comparar target com root
            if (compare(target.get(), root.get()) < 0) {
                // Se o valor do nó atual for maior que o valor do nó alvo,
                // o sucessor está em algum lugar à esquerda ou é o nó atual.
                successor = root;
                root = root.left;
            } else {
                // Caso contrário, vá para a subárvore direita.
                root = root.right;
            }
        }

        return successor;
    }

    public boolean isArvoreIgual(BinaryTree<T> arvore2) {
        return isArvoreIgual(this.root, arvore2.root);
    }

    private boolean isArvoreIgual(Node<T> arvore1, Node<T> arvore2) {
        if (arvore1 == null && arvore2 == null) {
            return true;
        }
        if (arvore1 != null && arvore2 != null && compare(arvore1.get(), arvore2.get()) == 0) {
            return isArvoreIgual(arvore1.getLeft(), arvore2.getLeft()) && isArvoreIgual(arvore1.getRight(), arvore2.getRight());
        }
        return false;
    }

    //metodo para percorrer em ordem iteravel
    public List<T> percorrerEmOrdem() {
        return percorrerEmOrdem(root);
    }

    private List<T> percorrerEmOrdem(Node<T> node) {
        List<T> result = new ArrayList<>();
        if (node != null) {
            result.addAll(percorrerEmOrdem(node.getLeft()));
            result.add(node.get());
            result.addAll(percorrerEmOrdem(node.getRight()));
        }
        return result;
    }

    public Node<T> rodaPelaDireita() {
        if (root == null) {
            return null;
        }
        root = rodaDireita(root);
        return root;
    }

    private Node<T> rodaDireita(Node<T> node) {
        if (node == null || node.getLeft() == null) {
            return node; // Não há rotação se o nó ou seu filho esquerdo for nulo
        }

        Node<T> newRoot = node.getLeft(); // O novo nó raiz será o filho esquerdo
        node.setLeft(newRoot.getRight()); // O filho direito do novo nó raiz torna-se o filho esquerdo do nó atual
        newRoot.setRight(node);           // O nó atual torna-se o filho direito do novo nó raiz

        node.pr = node.right == null ? 0 : node.right.getDepth();
        newRoot.pl = newRoot.left == null ? 0 : newRoot.left.getDepth();

        return newRoot;
    }

    public Node<T> rodaPelaEsquerda() {
        if (root == null) {
            return null;
        }
        root = rodaEsquerda(root);
        return root;
    }

    private Node<T> rodaEsquerda(Node<T> node) {
        if (node == null || node.getRight() == null) {
            return node; // Não houve rotação se o nó ou seu filho direito for nulo
        }

        Node<T> newRoot = node.getRight(); // O novo nó raiz é o filho direito
        node.setRight(newRoot.getLeft());  // O filho esquerdo do novo nó raiz torna-se o filho direito do nó atual
        newRoot.setLeft(node);             // O nó atual torna-se o filho esquerdo do novo nó raiz

        node.pl = node.left == null ? 0 : node.left.getDepth();
        newRoot.pr = newRoot.right == null ? 0 : newRoot.right.getDepth();

        return newRoot;
    }

    public Node<T> rodaduplaRL() {
        root = rodaduplaRL(root);
        return root;
    }

    private Node<T> rodaduplaRL(Node<T> node) {
        if (node == null) {
            return null;
        }
        node = rodaDireita(node);
        node = rodaEsquerda(node);
        return node;
    }

    public Node<T> rodaduplaLR() {
        root = rodaduplaLR(root);
        return root;
    }

    private Node<T> rodaduplaLR(Node<T> node) {
        if (node == null) {
            return null;
        }
        node = rodaEsquerda(node);
        node = rodaDireita(node);
        return node;
    }

    public int diferencaProfundidade(Node<T> node) {
        return node.pl - node.pr;
    }

    private Node<T> insere(Node<T> node, T value) {
        if (node == null) {
            return new Node<>(value);
        }
        int c = node.data.compareTo(value);

        if (c < 0) {
            node.left = insere(node.left, value);
            int pl = node.left.getDepth();
            node.pl = pl;
            if (!node.balanced()) {
                return autobalance(node, value);
            }
        }
        if (c > 0) {
            node.right = insere(node.right, value);
            int pr = node.right.getDepth();
            node.pr = pr;
            if (!node.balanced()) {
                return autobalance(node, value);
            }
        }
        if (c == 0) {
            if (node != null) throw new RuntimeException();
            return new Node<>(value);
        }
        return node;
    }
    private Node<T> autobalance (Node<T> node, T value){
        int l1 = node.data.compareTo(value);

        boolean LL = (l1>0) && (node.left.data.compareTo(value) > 0);
        boolean LR = (l1>0) && (node.left.data.compareTo(value) < 0);

        boolean RL = (l1<0) && (node.right.data.compareTo(value) > 0);
        boolean RR = (l1<0) && (node.right.data.compareTo(value) < 0);

        if (LL) return rodaEsquerda(node);
        if (LR) return rodaduplaLR(node);
        if (RL) return rodaduplaRL(node);
        if (RR) return rodaDireita(node);
        return node;
    }
}
