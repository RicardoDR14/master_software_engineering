package pt.isec.deis.pd.servidorBackup;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.sql.*;
import java.time.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Servidor de Backup para manter uma cópia sincronizada da base de dados do servidor principal.
 */
public class BackupServer {
    // Endereço multicast e porta para receber heartbeats
    private static final String MULTICAST_ADDRESS = "230.44.44.44";
    private static final int MULTICAST_PORT = 4444;

    // Tempo máximo sem receber heartbeats antes de encerrar (em milissegundos)
    private static final long HEARTBEAT_TIMEOUT = 30000;

    // diretoria onde a base de dados local será armazenada
    private final String dbDirectory;
    private Connection dbBackup;

    // Nome do arquivo da base de dados local
    private static final String DB_NAME = "backup_database.sqlite";

    // Versão atual da base de dados local
    private double localVersion = 0;

    // Último horário em que um heartbeat foi recebido
    private Instant lastHeartbeat;

    // Timer para verificar o timeout dos heartbeats
    private Timer heartbeatTimer;

    public BackupServer(String dbDirectory) {
        this.dbDirectory = dbDirectory;
    }

    /**
     * Método principal para iniciar o servidor de backup.
     */
    public void start() {
        try {
            // Verificar se a diretoria está vazia
            verifyAndInitializeDirectory();

            // Conectar-se ao servidor principal para obter uma cópia completa da base de dados
            synchronizeInitialDatabase();

            // Iniciar a escuta de heartbeats em uma thread separada
            Thread heartbeatListenerThread = new Thread(new HeartbeatListener());
            heartbeatListenerThread.start();

            // Iniciar o timer para monitorar os heartbeats
            startHeartbeatMonitor();

        } catch (Exception e) {
            System.err.println("Erro ao iniciar o servidor de backup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Verifica se a diretoria está vazia. Se não estiver, encerra a aplicação.
     */
    private void verifyAndInitializeDirectory() throws IOException {
        Path dirPath = Paths.get(dbDirectory);
        if (!Files.exists(dirPath)) {
            // Se a diretoria não existir, cria-a
            Files.createDirectories(dirPath);
            System.out.println("diretoria de backup criada em: " + dirPath.toAbsolutePath());
        } else {
            // Verifica se a diretoria está vazia
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
                if (stream.iterator().hasNext()) {
                    throw new IOException("diretoria de backup não está vazia. Encerrando o servidor de backup.");
                }
            }
        }
    }
    /**
     * Conecta-se ao servidor principal para obter uma cópia completa da base de dados.
     */
    private void synchronizeInitialDatabase() throws IOException, SQLException {

        String dbPath = Paths.get(dbDirectory, DB_NAME).toString();
        int mainServerPort = getMainServerPort();
        String mainServerAddress = "localhost";

        // Estabelece uma conexão TCP com o servidor principal
        try (Socket socket = new Socket(mainServerAddress, mainServerPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             FileOutputStream fos = new FileOutputStream(dbPath)) {

            // Envia o comando "get_database" para solicitar o banco de dados
            out.println("GET_DATABASE");
            System.out.println("Comando 'GET_DATABASE' enviado para o servidor.");

            try (InputStream in = socket.getInputStream()) {
                // Recebe o arquivo do banco de dados
                byte[] buffer = new byte[4096];
                int bytesRead;
                int n_bytes_lidos = 0;

                while ((bytesRead = in.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                    n_bytes_lidos += bytesRead;
                }

                System.out.println("Arquivo com " + n_bytes_lidos + " bytes lidos.");
                System.out.println("Banco de dados recebido e salvo em: " + dbPath);
            } catch (IOException o) {
                System.out.println("ERRO INPUT");
                o.printStackTrace();
            }


            // Estabelece conexão com o banco de dados recebido
            String jdbcUrl = "jdbc:sqlite:" + dbPath;
            try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
                System.out.println("Conexão estabelecida com sucesso ao banco de dados local!");
                // Realize operações no banco, se necessário
            }

        } catch (IOException e) {
            System.err.println("Erro ao receber a base de dados do servidor.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados local.");
            e.printStackTrace();
        }
    }

    /**
     * Método fictício para obter o porto do servidor principal.
     * Deve ser implementado conforme a lógica de obtenção do porto a partir do heartbeat ou configuração.
     */
    private int getMainServerPort() {
        // Implementar conforme necessário
        // Para este exemplo, retornamos um porto fixo
        return 5000;
    }

    /**
     * Atualiza a versão da base de dados local na tabela de versão.
     */
    private void updateLocalVersion(double newVersion) throws SQLException {
        String dbPath = Paths.get(dbDirectory, DB_NAME).toString();
        String url = "jdbc:sqlite:" + dbPath;

        try (Connection conn = DriverManager.getConnection(url)) {
            String updateVersion = "UPDATE db_version SET db_version = "+newVersion+" WHERE db_version = "+(newVersion-1);
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(updateVersion);
            }
            System.out.println("Versão local atualizada para: " + newVersion);
        }
    }

    /**
     * Inicia o timer que verifica se os heartbeats estão sendo recebidos dentro do tempo esperado.
     */
    private void startHeartbeatMonitor() {
        heartbeatTimer = new Timer(true); // Timer daemon
        heartbeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (lastHeartbeat != null) {
                    Duration duration = Duration.between(lastHeartbeat, Instant.now());
                    if (duration.toMillis() > HEARTBEAT_TIMEOUT) {
                        System.err.println("Nenhum heartbeat recebido nos últimos 30 segundos. Encerrando o servidor de backup.");
                        System.exit(0);
                    }
                }
            }
        }, HEARTBEAT_TIMEOUT, HEARTBEAT_TIMEOUT);
    }

    /**
     * Classe interna que implementa a escuta de heartbeats via multicast.
     */
    private class HeartbeatListener implements Runnable {
        @Override
        public void run() {
            try (MulticastSocket socket = new MulticastSocket(MULTICAST_PORT)) {
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                socket.joinGroup(group);

                System.out.println("Escutando heartbeats no grupo multicast " + MULTICAST_ADDRESS + ":" + MULTICAST_PORT);

                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength());
                    processHeartbeat(message);
                }
            } catch (IOException e) {
                System.err.println("Erro ao escutar heartbeats: " + e.getMessage());
                e.printStackTrace();
            }
        }

        /**
         * Processa a mensagem de heartbeat recebida.
         * O formato esperado é: "VERSION:<numero>;QUERY:<sql_query>"
         * Onde QUERY é opcional.
         */
        private void processHeartbeat(String message) {
            // Atualiza o horário do último heartbeat
            lastHeartbeat = Instant.now();

            System.out.println("Heartbeat recebido: " + message);

            // Parse da mensagem
            String[] parts = message.split(";");
            double receivedVersion = localVersion; // Inicializa com a versão local

            String query = null;

            for (String part : parts) {
                if (part.startsWith("VERSION:")) {
                    receivedVersion = Double.parseDouble(part.substring(8));
                } else if (part.startsWith("QUERY:")) {
                    query = part.substring(6);
                }
            }

            try {
                if (query.equals("NÃO HOUVE ATUALIZAÇÃO") || query.isEmpty()) {
                    // Heartbeat sem atualização, apenas verifica a versão
                    if (receivedVersion != localVersion) {
                        System.err.println("Versão recebida incompatível. Encerrando o servidor de backup.");
                        System.exit(0);
                    }
                } else {
                    // Heartbeat com query de atualização
                    if (receivedVersion == localVersion) {
                        // Aplica a query à base de dados local
                        applyUpdate(query);
                        localVersion++;
                        updateLocalVersion(localVersion);
                    } else {
                        System.err.println("Versão recebida fora de sequência. Encerrando o servidor de backup.");
                        System.exit(0);
                    }

                }
            } catch (Exception e) {
                System.err.println("Erro ao processar heartbeat: " + e.getMessage());
                e.printStackTrace();
                System.exit(0);
            }
        }

        /**
         * Aplica a query SQL recebida à base de dados local.
         */
        private void applyUpdate(String query) throws SQLException {
            String dbPath = Paths.get(dbDirectory, DB_NAME).toString();
            String url = "jdbc:sqlite:" + dbPath;

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(query);
                System.out.println("Query aplicada à base de dados local: " + query);
            }
        }
    }

    /**
     * Método principal para iniciar o servidor de backup.
     * Argumentos:
     * args[0] - Caminho do diretoria de armazenamento da base de dados
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java BackupServer <caminho_diretoria_backup>");
            System.exit(1);
        }

        String dbDirectory = args[0];
        BackupServer backupServer = new BackupServer(dbDirectory);
        backupServer.start();
    }
}

