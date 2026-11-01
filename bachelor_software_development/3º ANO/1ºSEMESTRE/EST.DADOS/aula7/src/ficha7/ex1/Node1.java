package ficha7.ex1;

public class Node1<T>{

    private T data;
    public Node1<T> left,right;

    // Nó sentinela será um nó estático e compartilhado por todas as instâncias
    public static final Node1<?> SENTINELA = new Node1<>(null);  // Nó sentinela

    public Node1(T data) {
        this.data = data;
        //this.left = null;
        //this.right = null;
        this.left = (Node1<T>) SENTINELA; //ex5
        this.right = (Node1<T>) SENTINELA; //ex5
    }

    public Node1 getLeft(){
        return left;
    }

    public Node1 getRight(){
        return right;
    }

    public T get(){
        return data;
    }

    public void set(T data){
        this.data = data;
    }

    public void setLeft(Node1 left){
        this.left = left;
    }

    public void setRight(Node1<T> right) {
        this.right = right;
    }

    //ex5
    public boolean isSentinela(){
        return this == SENTINELA;
    }


}
