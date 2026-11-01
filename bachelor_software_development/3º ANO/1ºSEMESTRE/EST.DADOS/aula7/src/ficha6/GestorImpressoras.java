package ficha6;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;

public class GestorImpressoras {
    HashMap<String,Impressora> mapImpressoras = new HashMap<>();
    PriorityQueue<Impressora> trabalhosImpressoras = new PriorityQueue<>(new Comparator<Impressora>() {
        @Override
        public int compare(Impressora o1, Impressora o2) {
            int m = o1.numTrabalhos()-o2.numTrabalhos();
            if (m==0) return o1.getPaginasTrabalhos()-o2.getPaginasTrabalhos();
            return o1.numTrabalhos()-o2.numTrabalhos();
        }
    });

    public void acrescentaImpressora(String nome, Impressora i){mapImpressoras.put(nome, i);}

    public void removeImpressora(String nome){mapImpressoras.remove(nome);}

    public boolean existeImpressora(String nome){return mapImpressoras.containsKey(nome);}

    public Impressora getImpressora(String nome){return mapImpressoras.get(nome);}

    public Set<String> getNomeImpressoras(){return mapImpressoras.keySet();}


    public void imprime(Trabalho trabalho){
        Impressora escolhida = trabalhosImpressoras.poll();
        escolhida.enviaTrabalho(trabalho);
        trabalhosImpressoras.add(escolhida);
    }
}
