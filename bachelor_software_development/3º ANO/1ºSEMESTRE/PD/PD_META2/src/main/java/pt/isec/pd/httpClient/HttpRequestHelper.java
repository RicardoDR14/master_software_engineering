package pt.isec.pd.httpClient;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpRequestHelper {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static String sendRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Codigo de resposta: " + response.statusCode());
            System.out.println("Conteudo da resposta: " + response.body());
            return response.body();
        } catch (Exception e) {
            System.err.println("Erro ao enviar o pedido: " + e.getMessage());
            return null;
        }
    }
}
