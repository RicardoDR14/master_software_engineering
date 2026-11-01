package pt.isec.pd.aula4;


import java.io.Serializable;

public class Time implements Serializable {

    private static final long serialVersionUID = 20L;

    private int hour, minutes, seconds;

    public Time(int h, int m, int s){
        this.hour=h;
        this.minutes=m;
        this.seconds=s;
    }

    public int getHour() {
        return hour;
    }
}
