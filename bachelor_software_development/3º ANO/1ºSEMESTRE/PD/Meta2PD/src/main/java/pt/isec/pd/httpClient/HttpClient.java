package pt.isec.pd.httpClient;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Scanner;

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

    public void enterGroup(Scanner scanner) {
        if (!tokenManager.isAuthenticated()) return;

        System.out.print("Digita o nome do grupo: ");
        String groupName = scanner.nextLine().trim();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/grupos/entrarGrupo?nomeGrupo=" + groupName)).header("Authorization", "Bearer " + tokenManager.getToken()).POST(HttpRequest.BodyPublishers.noBody()).build();

        sendRequest(request);
    }

    public void addExpense(Scanner scanner, String emailWhoRegistered) {
        if (!tokenManager.isAuthenticated()) {
            System.out.println("Não autorizado: Token inválido ou ausente.");
            return;
        }

        System.out.println("Indique o nome do grupo:");
        String nomeGrupoDespesa = scanner.nextLine();
        System.out.println("Descrição da despesa:");
        String descricaoDespesa = scanner.nextLine();
        System.out.println("Valor da despesa:");
        double valorDespesa = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Data da despesa (formato YYYY-MM-DD):");
        String dataDespesa = scanner.nextLine();
        System.out.println("Indique os usernames dos participantes (separados por vírgula):");
        String participantesInput = scanner.nextLine();

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

        sendRequest(request);
    }


    public void getExpenses(Scanner scanner) {
        if (!tokenManager.isAuthenticated()) return;

        System.out.print("Digita o nome do grupo: ");
        String groupName = scanner.nextLine().trim();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/despesas/" + groupName)).header("Authorization", "Bearer " + tokenManager.getToken()).GET().build();

        sendRequest(request);
    }

    public void deleteExpense(Scanner scanner) {
        if (!tokenManager.isAuthenticated()) return;

        System.out.print("Digita o nome do grupo: ");
        String groupName = scanner.nextLine().trim();

        System.out.print("Digita o id da despesa: ");
        int expenseId = scanner.nextInt();
        scanner.nextLine();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/despesas/" + groupName + "/" + expenseId)).header("Authorization", "Bearer " + tokenManager.getToken()).DELETE().build();

        sendRequest(request);
    }

    private void sendRequest(HttpRequest request) {
        HttpRequestHelper.sendRequest(request);
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