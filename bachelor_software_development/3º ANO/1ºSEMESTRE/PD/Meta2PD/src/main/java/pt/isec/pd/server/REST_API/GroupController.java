package pt.isec.pd.server.REST_API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.isec.pd.server.Data.Grupo;
import pt.isec.pd.server.Database.ManagerDB;

import java.util.List;

@RestController
@RequestMapping("/grupos")
public class GroupController {

    @Autowired
    private ManagerDB managerDB;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/entrarGrupo")
    public ResponseEntity<String> entrarGrupo(@RequestParam String nomeGrupo, @RequestHeader("Authorization") String authorizationHeader) {

        if (!tokenService.validateTokenFromRequest(authorizationHeader)) {
            return new ResponseEntity<>("Não autorizado: Token inválido ou ausente.", HttpStatus.UNAUTHORIZED);
        }

        String email = tokenService.extractEmail(authorizationHeader.substring(7));

        boolean grupoCriado = managerDB.criarGrupo(nomeGrupo, email);
        if (!grupoCriado) {
            return new ResponseEntity<>("Erro ao criar o grupo!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Grupo criado com sucesso!", HttpStatus.OK);
    }

    @GetMapping("/listarGrupos")
    public ResponseEntity<?> listarGrupos(@RequestHeader("Authorization") String cabecalhoAutorizacao) {
        if (!tokenService.validateTokenFromRequest(cabecalhoAutorizacao)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Não autorizado: Token inválido ou ausente.");
        }
        String email = tokenService.extractEmail(cabecalhoAutorizacao.substring(7));
        List<Grupo> grupos = managerDB.obterGrupos(email);

        if (grupos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("O utilizador não faz parte de nenhum grupo.");
        }
        return ResponseEntity.ok(grupos);
    }
}
