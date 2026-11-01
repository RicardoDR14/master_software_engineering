package pt.isec.pd.spring_boot.exemplo1;

import java.io.*;
import java.io.BufferedReader;
import java.net.*;
import java.util.Base64;
import java.util.Scanner;

public class PdRestApiComSegurancaConsumer {

    public static String sendRequestAndShowResponse(String uri, String verb, String authorizationValue, String contentType, byte [] body) throws IOException, URISyntaxException {

        String responseBody = null;
        URL url = new URI(uri).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(verb);
        connection.setRequestProperty("Accept", "*/*");

        if(authorizationValue!=null) {
            connection.setRequestProperty("Authorization", authorizationValue);
        }

        if(body!=null){
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", contentType);
            connection.getOutputStream().write(body);
        }

        connection.connect();

        int responseCode = connection.getResponseCode();
        System.out.println("Response code: " +  responseCode + " (" + connection.getResponseMessage() + ")");

        Scanner s;

        if(connection.getErrorStream()!=null) {
            s = new Scanner(connection.getErrorStream());
            responseBody = s.hasNextLine() ? s.nextLine() : null;
        }

        try {
            s = new Scanner(connection.getInputStream());
            responseBody = s.hasNextLine() ? s.nextLine() : null;
        } catch (IOException e){}

        connection.disconnect();

        System.out.println(verb + " " + uri + (body==null ? "" : " with body content") + System.lineSeparator() +
                System.lineSeparator() + " ==> " + responseBody);
        System.out.println();

        return responseBody;
    }

    public static void main(String args[]) throws URISyntaxException, IOException {
        String helloUri = "http://localhost:8080/hello/fr?name=Jeanne";
        String helloUri2 = "http://localhost:8080/hello/gr?name=Jeanne";
        String loginUri = "http://localhost:8080/login";
        String loremUri = "http://localhost:8080/lorem";
        String harryPotterUri = "http://localhost:8080/movies/harry-potter/characters";

        System.out.println();

        //OK
        sendRequestAndShowResponse(helloUri, "GET", null, null, null);

        //Língua "gr" não suportada
        sendRequestAndShowResponse(helloUri2, "GET", null, null, null);

        //Falta um campo "Authorization: bearer ..." no cabeçalho do pedido com um token JWT válido
        sendRequestAndShowResponse(loremUri, "GET", null, null, null);

        //Falta um campo "Authorization: basic ..." válido no cabeçalho do pedido para autenticação básica
        String jwtToken = sendRequestAndShowResponse(loginUri, "POST",null, null, null);

        //OK
        String credentials = Base64.getEncoder().encodeToString("admin:admin".getBytes());
        jwtToken = sendRequestAndShowResponse(loginUri, "POST","basic "+ credentials, null, null); //Base64(admin:admin) YWRtaW46YWRtaW4=

        //OK
        sendRequestAndShowResponse(loremUri+"?type=word&length=6", "GET", "bearer " + jwtToken, null, null);

        //PUT não suportado para esta URI
        sendRequestAndShowResponse(loremUri, "PUT", "bearer " + jwtToken, null, null);

        //POST sem corpo de mensagem
        sendRequestAndShowResponse(loremUri, "POST", "bearer " + jwtToken, null, null);

        //4a não int
        sendRequestAndShowResponse(loremUri, "POST", "bearer " + jwtToken, "application/json", "{\"type\":\"word\",\"length\":4a}".getBytes());

        //Ok
        sendRequestAndShowResponse(loremUri, "POST", "bearer " + jwtToken, "application/json", "{\"type\":\"word\",\"length\":4}".getBytes());

        //Ok
        sendRequestAndShowResponse(harryPotterUri + "/" + "images", "GET", "bearer " + jwtToken, null, null);

    }
}