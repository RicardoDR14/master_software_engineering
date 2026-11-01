package pt.isec.pd.tcp.file;

import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ServidorTcp {

    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";

    public static void main(String[] args) {

        int listeningPort;
        String request, timeMsg;
        Calendar calendar;

        if (args.length != 1) {
            System.out.println("Sintaxe: java TcpTimeServer listeningPort");
            return;
        }

        listeningPort = Integer.parseInt(args[0]);

        try (ServerSocket socket = new ServerSocket(listeningPort)) {

            System.out.println("Tcp Time Server iniciado...");

            while (true) {

                try (Socket nextClient = socket.accept();
                     PrintWriter out = new PrintWriter(nextClient.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(nextClient.getInputStream()))
                ) {

                    nextClient.setSoTimeout(1000);
                    request = in.readLine();

                    if (request != null) {
                        continue;
                    }

                    System.out.println("Recebido\"" + request.trim() + "\" de " + nextClient.getInetAddress() + ":" + nextClient.getPort());

                    calendar = GregorianCalendar.getInstance();
                    timeMsg = calendar.get(GregorianCalendar.HOUR_OF_DAY) + ":" + calendar.get(GregorianCalendar.MINUTE) + ":" + calendar.get(GregorianCalendar.SECOND);

                    out.println(timeMsg);
                    out.flush();

                } catch (IOException e) {
                    System.out.println("Erro na comunicação do cliente atual\n\n");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        }
    }
}
