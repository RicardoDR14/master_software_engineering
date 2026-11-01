package ficha6;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Impressora {
    String nome, marca, modelo;
    PriorityQueue<Trabalho> filaDeEspera = new PriorityQueue<>(new Comparator<Trabalho>() {
        @Override
        public int compare(Trabalho o1, Trabalho o2) {
            return o1.getPaginas() - o2.getPaginas();
        }
    });

    public Impressora(String nome, String marca, String modelo) {
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
        return String.format("Impressora -> Nome: %s, Marca: %s, Modelo: %s\n", nome, marca, modelo);
    }

    public boolean temProximoTrabalho() {
        return !filaDeEspera.isEmpty();
    }

    public void enviaTrabalho(Trabalho t) {
        filaDeEspera.add(t);
    }

    public Trabalho proximoTrabalho() {
        return filaDeEspera.poll();
    }

    public int numTrabalhos() {
        return filaDeEspera.size();
    }

    public int getPaginasTrabalhos() {
        int pags = 0;
        for (Trabalho t : filaDeEspera) pags += t.getPaginas();
        return pags;
    }
}
