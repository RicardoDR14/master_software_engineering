package pt.isec.pd.server.Data;

public class Grupo {

    private final int id;
    private final String nomeGrupo;

    public Grupo(int id, String nomeGrupo) {
        this.id = id;
        this.nomeGrupo = nomeGrupo;
    }

    public int getId() {
        return id;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }
}