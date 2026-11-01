package ficha6;

public class Impressora {
    String nome, marca, modelo;

    public Impressora(String nome, String marca, String modelo){
        this.nome = nome;
        this.marca = marca;
        this.modelo = modelo;
    }

    public String getNome() {
        return nome;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    @Override
    public String toString() {
        return String.format("Impressora -> Nome: %s, Marca: %s, Modelo: %s\n",nome,marca,modelo);
    }
}
