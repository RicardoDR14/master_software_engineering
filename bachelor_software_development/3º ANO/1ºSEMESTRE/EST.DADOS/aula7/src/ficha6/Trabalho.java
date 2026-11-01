package ficha6;

public class Trabalho {
    private String nome_ficheiro;
    private int pagi, pagf;

    public Trabalho(String nome_ficheiro, int pagi, int pagf) {
        this.nome_ficheiro = nome_ficheiro;
        this.pagi = pagi;
        this.pagf = pagf;
    }

    public String getNome_ficheiro() {
        return nome_ficheiro;
    }

    public void setNome_ficheiro(String nome_ficheiro) {
        this.nome_ficheiro = nome_ficheiro;
    }

    public int getPaginas() { return pagf-pagi;}
}
