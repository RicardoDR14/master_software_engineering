package pt.isec.pd.server.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class Utilizador {

    private int id=0;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format <user@example.com>")
    private final String email;

    @NotBlank(message = "Password cannot be empty")
    private final String password;

    @NotBlank(message = "Cellphone cannot be empty")
    private final String telefone;

    public Utilizador( int id, String email, String password, String telefone) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getTelefone() {
        return telefone;
    }
}
