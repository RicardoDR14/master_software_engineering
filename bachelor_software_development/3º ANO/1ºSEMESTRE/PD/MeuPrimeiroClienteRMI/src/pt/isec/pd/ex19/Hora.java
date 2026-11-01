package pt.isec.pd.ex19;

import java.io.Serial;
import java.io.Serializable;

public class Hora implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int horas;
    protected int minutos;
    protected int segundos;

    public Hora(int horas, int minutos, int segundos) {
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;
    }

    public int getHoras() {
        return horas;
    }

    public int getMinutos() {
        return minutos;
    }

    public int getSegundos() {
        return segundos;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }
}

