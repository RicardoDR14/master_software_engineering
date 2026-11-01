package pt.isec.pd.server.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class Despesa {

    private int id = 0;
    private int idGrupo = 0;
    @NotBlank(message = "Description cannot be empty.")
    private String descricao;
    @NotBlank(message = "Date cannot be empty.")
    private String data;
    @Positive(message = "Total value must be greater than zero.")
    private double valorTotal;
    private String emailRegistou;

    @Override
    public String toString() {
        return "Despesa{" + "id=" + id + ", idGrupo=" + idGrupo + ", descricao='" + descricao + '\'' + ", data='" + data + '\'' + ", valorTotal=" + valorTotal + ", emailRegistou='" + emailRegistou + '\'' + ", emailParticipantes=" + emailParticipantes + '}';
    }

    @NotNull(message = "List of users to pay cannot be null.")
    private String emailParticipantes;

    public Despesa(int idGrupo, int id, String descricao, String data, double valorTotal, String emailRegistou, String emailParticipantes) {
        this.id = id;
        this.idGrupo = idGrupo;
        this.descricao = descricao;
        this.data = data;
        this.valorTotal = valorTotal;
        this.emailRegistou = emailRegistou;
        this.emailParticipantes = emailParticipantes;
    }

    public int getId() {
        return id;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getData() {
        return data;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public String getEmailRegistou() {
        return emailRegistou;
    }

    public String getEmailToPayList() {
        return emailParticipantes;
    }

    public String getEmailToPayString() {
        return String.join(" ", emailParticipantes);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }


    public void setEmailRegistou(String emailRegistou) {
        this.emailRegistou = emailRegistou;
    }


    public void setEmailParticipantes(String emailParticipantes) {
        this.emailParticipantes = emailParticipantes;
    }
}
