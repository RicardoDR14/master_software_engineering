package pt.isec.pd.httpClient;

import java.net.URI;
import java.net.http.HttpRequest;

public class HttpClient {
    private final TokenManager tokenManager = new TokenManager();

    public boolean register(String email, String password, String telefone) {

        String json = String.format("""
                {
                    "email": "%s",
                    "password": "%s",
                    "telefone": "%s"
                }
                """, email, password, telefone);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/utilizadores/registo")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();

        sendRequestWithToken(request);

        return tokenManager.isAuthenticated();
    }

    public boolean login(String email, String password) {

        String json = String.format("""
                {
                    "email": "%s",
                    "password": "%s"
                }
                """, email, password);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/utilizadores/login")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        sendRequestWithToken(request);

        return tokenManager.isAuthenticated();
    }

    public void listGroups() {
        if (!tokenManager.isAuthenticated()) return;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/grupos/listarGrupos")).header("Authorization", "Bearer " + tokenManager.getToken()).GET().build();

        sendRequest(request);
    }

    public void enterGroup(String groupName) {
        if (!tokenManager.isAuthenticated()) return;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/grupos/entrarGrupo?nomeGrupo=" + groupName)).header("Authorization", "Bearer " + tokenManager.getToken()).POST(HttpRequest.BodyPublishers.noBody()).build();

        sendRequest(request);
    }

    public boolean addExpense(String nomeGrupoDespesa, String descricaoDespesa, String dataDespesa, double valorDespesa, String emailWhoRegistered, String participantesInput) {
        if (!tokenManager.isAuthenticated()) {
            System.out.println("Não autorizado: Token inválido ou ausente.");
            return false;
        }

        String json = String.format("""
                {
                    "descricao": "%s",
                    "data": "%s",
                    "valorTotal": %s,
                    "emailRegistou": "%s",
                    "emailParticipantes": "%s"
                }
                """, descricaoDespesa, dataDespesa, valorDespesa, emailWhoRegistered, participantesInput);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/despesas/" + nomeGrupoDespesa)).header("Authorization", "Bearer " + tokenManager.getToken()).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();

        String response = sendRequest(request);

        if (response != null && response.contains("Despesa adicionada")) {
            return true;
        }
        return true;
    }


    public void getExpenses(String groupName) {
        if (!tokenManager.isAuthenticated()) return;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/despesas/" + groupName)).header("Authorization", "Bearer " + tokenManager.getToken()).GET().build();

        sendRequest(request);
    }

    public boolean deleteExpense(String groupName, int expenseId) {
        if (!tokenManager.isAuthenticated()) return false;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/despesas/" + groupName + "/" + expenseId)).header("Authorization", "Bearer " + tokenManager.getToken()).DELETE().build();

        String response = sendRequest(request);

        if (response != null && response.contains("Despesa removida com sucesso")) {
            return true;
        }
        return true;
    }

    private String sendRequest(HttpRequest request) {
        return HttpRequestHelper.sendRequest(request);
    }

    private void sendRequestWithToken(HttpRequest request) {
        String response = HttpRequestHelper.sendRequest(request);
        if (response != null) {
            String token = extractTokenFromResponse(response);
            tokenManager.setToken(token);
        }
    }

    private String extractTokenFromResponse(String response) {
        String tokenPrefix = "Token: ";
        int tokenStartIndex = response.indexOf(tokenPrefix);

        if (tokenStartIndex == -1) {
            System.out.println("Token not found in response.");
            return null;
        }
        tokenStartIndex += tokenPrefix.length();
        return response.substring(tokenStartIndex).trim();
    }
}