package pt.isec.pd.tcp.file.client;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jose' Marinho
 */
class TrataCliente implements Runnable {
    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 10; //segundos
    private final Socket socketToClient;
    private final File localDirectory;

    public TrataCliente(Socket s, File ld) {
        socketToClient = s;
        localDirectory = ld;
    }

    @Override
    public void run() {
        int nbytes;
        byte[] fileChunk = new byte[MAX_SIZE];
        String requestedCanonicalFilePath = null;
        try (
                BufferedReader bin = new BufferedReader(new InputStreamReader(socketToClient.getInputStream()))
        ) { //2 (atendimento cliente)

            socketToClient.setSoTimeout(TIMEOUT * 1000);
            OutputStream out = socketToClient.getOutputStream();

            String requestedFileName = bin.readLine();

            if (requestedFileName == null)
                return;

            System.out.println("Recebido pedido para \"" + requestedFileName + "\" de " + socketToClient.getInetAddress().getHostName() + ":" + socketToClient.getPort());

            requestedCanonicalFilePath = new File(localDirectory + File.separator + requestedFileName).getCanonicalPath();

            if (!requestedCanonicalFilePath.startsWith(localDirectory.getCanonicalPath() + File.separator)) {
                System.out.println("Nao e' permitido aceder ao ficheiro " + requestedCanonicalFilePath + "!");
                System.out.println("A directoria de base nao corresponde a " + localDirectory.getCanonicalPath() + "!");
                return;
            }

            try (InputStream requestedFileInputStream = new FileInputStream(requestedCanonicalFilePath)) {
                System.out.println("Ficheiro " + requestedCanonicalFilePath + " aberto para leitura.");

                int totalBytes = 0;
                int nChunks = 0;

                do {
                    nbytes = requestedFileInputStream.read(fileChunk);

                    if (nbytes > -1) {//Not EOF
                        out.write(fileChunk, 0, nbytes);
                        out.flush();

                        totalBytes += nbytes;
                        nChunks++;
                    }

                } while (nbytes > 0);

                System.out.format("Transferencia concluida em %d blocos com um total de %d bytes\r\n", nChunks, totalBytes);
            }

        }  catch (SocketTimeoutException ex) { //Subclasse de IOException
            System.out.println("O cliente atual nao enviou qualquer nome de ficheiro (timeout)");
        } catch (FileNotFoundException e) {   //Subclasse de IOException
            System.out.println("Ocorreu a excepcao {" + e + "} ao tentar abrir o ficheiro " + requestedCanonicalFilePath + "!");
        } catch (IOException ex) {
            System.out.println("Problem de I/O no atendimento ao cliente atual: " + ex);
        }
    }
}

public class GetFileTcpServer {
    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 10; //segundos


    public static void main(String[] args) {

        File localDirectory;
        String requestedFileName;
        String requestedCanonicalFilePath = null;
        OutputStream out;
        byte[] fileChunk = new byte[MAX_SIZE];
        int nbytes;

        if (args.length != 2) {
            System.out.println("Sintaxe: java GetFileUdpServer listeningPort localRootDirectory");
            return;
        }

        localDirectory = new File(args[1].trim());

        if (!localDirectory.exists()) {
            System.out.println("A directoria " + localDirectory + " nao existe!");
            return;
        }

        if (!localDirectory.isDirectory()) {
            System.out.println("O caminho " + localDirectory + " nao se refere a uma directoria!");
            return;
        }

        if (!localDirectory.canRead()) {
            System.out.println("Sem permissoes de leitura na directoria " + localDirectory + "!");
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]))) { // 1

            while (true) {

                Socket socket = serverSocket.accept();
                // Cria e lança thread p atender novo cliente
                new Thread(new TrataCliente(socket,localDirectory)).start();


            } //while

        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo:\n\t" + e);
        } catch (SocketException e) {
            System.out.println("Ocorreu uma excepcao ao nivel do socket UDP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu a excepcao de E/S: \n\t" + e);
        } //try 1
    } // main
}

