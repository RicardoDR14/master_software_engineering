package ficha6;

import java.util.HashMap;

public class GestorImpressoras {
    HashMap<String,Impressora> mapImpressoras = new HashMap<>();

    public void adicionarImpressora(String nome, Impressora i){
        mapImpressoras.put(nome, i);
    }

    public Impressora getImpressora(String nome){
        return mapImpressoras.get(nome);
    }
}
