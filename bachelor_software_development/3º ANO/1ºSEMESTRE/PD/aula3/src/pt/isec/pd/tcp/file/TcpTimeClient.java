package pt.isec.pd.tcp.file;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class TcpTimeClient {

    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME\n\n";
    public static final int TIMEOUT = 10; //segundos

    public static void main(String[] args) {


        int serverPort = -1;
        String response;

        if (args.length != 2) {
            System.out.println("Sintaxe: java TcpTimeClient serverAddress serverUdpPort");
            return;
        }


        serverPort = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(args[0], serverPort)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            socket.setSoTimeout(TIMEOUT * 1000); //ms

            out.println(TIME_REQUEST);
            //out.flush();

            response = in.readLine();
            System.out.println(response);

        } catch (UnknownHostException e) {
            System.out.println("Destino desconhecido:\n\t" + e);
        } catch (NumberFormatException e) {
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        } catch (SocketTimeoutException e) {
            System.out.println("Nao foi recebida qualquer resposta:\n\t" + e);
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        }
    }

}
