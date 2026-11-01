package pt.isec.pd.spring_boot.exemplo1.models;

import java.util.List;

public class MovieCharacter {
    protected int index;
    protected String fullName;
    protected String nickname;
    protected String hogwartsHouse;
    protected String interpretedBy;
    java.util.List<String> children;
    String image;
    String birthdate;

    public int getIndex() {
        return index;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHogwartsHouse() {
        return hogwartsHouse;
    }

    public String getInterpretedBy() {
        return interpretedBy;
    }

    public List<String> getChildren() {
        return children;
    }

    public String getImage() {
        return image;
    }

    public String getBirthdate() {
        return birthdate;
    }

    @Override
    public String toString() {
        return "index: " + index + System.lineSeparator() +
                "fullName: " + fullName + System.lineSeparator() +
                "birthdate: " + birthdate + System.lineSeparator() +
                "hogwartsHouse: " + hogwartsHouse + System.lineSeparator() +
                "interpretedBy: " + interpretedBy + System.lineSeparator() +
                "image: " + image + System.lineSeparator() +
                "children: " + children + System.lineSeparator();
    }
}
