package avl;

public class Node <T>{

    T data;
    public Node<T> left,right;

    int pl, pr; // profundidade das subarvores direta e esquerda

    // Nó sentinela será um nó estático e compartilhado por todas as instâncias
    public static final Node<?> SENTINELA = new Node<>(null);  // Nó sentinela

    public Node(T data) {
        this.data = data;
        //this.left = null;
        //this.right = null;
        this.left = (Node<T>) SENTINELA; //ex5
        this.right = (Node<T>) SENTINELA; //ex5
    }

    public Node getLeft(){
        return left;
    }

    public Node getRight(){
        return right;
    }

    public T get(){
        return data;
    }

    public void set(T data){
        this.data = data;
    }

    public void setLeft(Node left){
        this.left = left;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    //ex5
    public boolean isSentinela(){
        return this == SENTINELA;
    }

    public int getDepth(){
        return (pl>pr)?pl+1:pr+1;//profundidade do nó é a da maior subarvore + 1
    }

    boolean balanced(){
        return Math.abs(pl-pr) <= 1;
    }
}
