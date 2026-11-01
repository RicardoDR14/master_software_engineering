package pt.isec.pd.server.REST_API;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.isec.pd.server.Data.Despesa;
import pt.isec.pd.server.Database.ManagerDB;
import pt.isec.pd.server.RMI.RMIControllers;

import java.util.List;

@RestController
@RequestMapping("/despesas")
public class ExpenseController {

    @Autowired
    private ManagerDB managerDB;

    @Autowired
    private TokenService tokenService;


    @PostMapping("/{nomeGrupo}")
    public ResponseEntity<String> addExpense(@PathVariable String nomeGrupo, @Valid @RequestBody Despesa despesa, @RequestHeader("Authorization") String authorizationHeader) {

        if (!tokenService.validateTokenFromRequest(authorizationHeader)) {
            return ResponseEntity.status(401).body("Não autorizado: Token inválido ou ausente.");
        }

        String userEmail = tokenService.extractEmail(authorizationHeader.substring(7));

        int res = managerDB.inserirDespesa(userEmail, nomeGrupo, despesa.getDescricao(), despesa.getValorTotal(), despesa.getEmailToPayList(), despesa.getData());

        if (res == -1) {
            return ResponseEntity.badRequest().body("Grupo não existe ou Utilizador não pertence ao grupo.");
        }

        if (res == -2) {
            return ResponseEntity.badRequest().body("Não foi possivel inserir despesa.");
        }

        if (res == -3) {
            return ResponseEntity.badRequest().body("Não foi possivel inserir participação.");
        }

        if (res>0) {
            return ResponseEntity.ok("Despesa adicionada. ID: " + res);
        } else {
            return ResponseEntity.status(500).body("Erro ao adicionar despesa.");
        }
    }

    @GetMapping("/{nomeGrupo}")
    public ResponseEntity<?> getExpenses(@PathVariable String nomeGrupo, @RequestHeader("Authorization") String authorizationHeader) {

        if (!tokenService.validateTokenFromRequest(authorizationHeader)) {
            return ResponseEntity.status(401).body("Não autorizado: Token inválido ou ausente.");
        }

        String userEmail = tokenService.extractEmail(authorizationHeader.substring(7));

        int groupId = managerDB.obterIdGrupo(nomeGrupo);
        if (groupId == -1) {
            return ResponseEntity.badRequest().body("Grupo não existe.");
        }

        List<Despesa> despesas = managerDB.obterHistoricoDespesas(nomeGrupo,userEmail);
        if (despesas.isEmpty()) {
            return ResponseEntity.status(403).body("Utilizador não pertence ao grupo.");
        }
        return ResponseEntity.ok(despesas);
    }

    @DeleteMapping("/{nomeGrupo}/{idDespesa}")
    public ResponseEntity<String> deleteExpense(@PathVariable String nomeGrupo, @PathVariable int idDespesa, @RequestHeader("Authorization") String authorizationHeader) {

        if (!tokenService.validateTokenFromRequest(authorizationHeader)) {
            return ResponseEntity.status(401).body("Não autorizado: Token inválido ou ausente.");
        }

        String userEmail = tokenService.extractEmail(authorizationHeader.substring(7));

        int groupId = managerDB.obterIdGrupo(nomeGrupo);
        if (groupId == -1) {
            return ResponseEntity.badRequest().body("Grupo não existe.");
        }

        int userEmailId = managerDB.obterIdUtilizador(userEmail);
        if (userEmailId == -1) {
            return ResponseEntity.badRequest().body("Utilizador não existe.");
        }

        boolean userInGroup = managerDB.utilizadorNoGrupo(groupId, userEmailId);
        if (!userInGroup) {
            return ResponseEntity.status(403).body("Utilizador nao pertence ao grupo.");
        }

        boolean success = managerDB.eliminarDespesa(idDespesa);
        if (success) {
            return ResponseEntity.ok("Despesa eliminada com sucesso.");
        } else {
            return ResponseEntity.status(404).body("Despesa não encontrada.");
        }
    }
}
