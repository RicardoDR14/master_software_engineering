package pt.isec.pd.server.REST_API;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pt.isec.pd.server.Data.LoginRequest;
import pt.isec.pd.server.Data.Utilizador;
import pt.isec.pd.server.Database.ManagerDB;
import pt.isec.pd.server.RMI.RMIControllers;

@RestController
@RequestMapping("/utilizadores")
@Validated
public class UserController {

    @Autowired
    private ManagerDB managerDB;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RMIControllers rmiControllers;

    @PostMapping("/registo")
    public ResponseEntity<String> registarUtilizador(@Valid @RequestBody Utilizador utilizador) {

        if (managerDB.utilizadorExiste(utilizador.getEmail())) {
            return new ResponseEntity<>("O utilizador já existe!", HttpStatus.BAD_REQUEST);
        }

        boolean sucesso = managerDB.registoUtilizador(utilizador.getEmail(), utilizador.getPassword(), utilizador.getTelefone());
        if (sucesso) {
            String token = tokenService.generateToken(utilizador.getEmail());
            rmiControllers.notificarClientes("Novo utilizador registado: " + utilizador.getEmail());
            return new ResponseEntity<>("Utilizador registado com sucesso! Token: " + token, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Erro ao registar o utilizador!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> autenticarUtilizador(@Valid @RequestBody LoginRequest loginRequest) {
        if (managerDB.autenticarUtilizador(loginRequest.getEmail(), loginRequest.getPassword())) {
            String token = tokenService.generateToken(loginRequest.getEmail());
            rmiControllers.notificarClientes("Utilizador autenticado: " + loginRequest.getEmail());
            return new ResponseEntity<>("Login bem-sucedido! Token: " + token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Credenciais inválidas!", HttpStatus.UNAUTHORIZED);
        }
    }
}
