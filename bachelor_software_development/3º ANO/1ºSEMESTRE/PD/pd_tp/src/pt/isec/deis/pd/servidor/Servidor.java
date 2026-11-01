package pt.isec.deis.pd.servidor;

import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;


public class Servidor {

    private static final int HEARTBEAT_INTERVAL = 10000; // Intervalo
    private static final int HEARTBEAT_PORT = 4444; //Porta de multicast
    private static final String HEARTBEAT_ADDRESS = "230.44.44.44"; //Address de multicast
    private static final int TIMEOUT = 5000000;
    private static double currentVersion = 0.0;

    /*public static double getVersion(String dbPath) {
        String url = "jdbc:sqlite:" + dbPath;
        String sql = "SELECT db_version FROM db_version";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {

            stmt.execute(sql);
            ResultSet resultSet = stmt.getResultSet();

            if (resultSet.next()) {
                return resultSet.getDouble("db_version");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }*/

    public static void upVersionDB(Connection db) throws SQLException {/*
        String url = "jdbc:sqlite:" + dbPath;*/

        /*try (Connection connection = DriverManager.getConnection(url)) {
            connection.setAutoCommit(false);*/

        double newVersion = currentVersion + 1;  // Incrementa a versão

        // Passo 2: Atualizar a versão
        String updateSql = "UPDATE db_version SET db_version = " + newVersion + " WHERE db_version = " + currentVersion;

        try (Statement updateStatement = db.createStatement()) {
            updateStatement.executeUpdate(updateSql);
            System.out.println("Versão atualizada para: " + newVersion);
            currentVersion = newVersion;
        }

   /* } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados ou executar SQL.");
            e.printStackTrace();
        }*/
    }


    public static boolean loginUtilizador(String email, String password, String dbPath) {

        String url = "jdbc:sqlite:" + dbPath;

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {

            String sql = "SELECT * FROM utilizador WHERE email = '" + email + "' AND password = '" + password + "'";

            stmt.execute(sql);
            ResultSet resultSet = stmt.getResultSet();

            return resultSet.next();

        } catch (SQLException e) {

            System.out.println("Erro ao conectar à base de dados: " + e.getMessage());
            return false;
        }

    }

    public static boolean registoUtilizador(String email, String password, String telefone, String dbPath) {

        String url = "jdbc:sqlite:" + dbPath;

        try (Connection conn = DriverManager.getConnection(url)) {
            try (Statement stmt = conn.createStatement()) {

                String sql = "INSERT INTO utilizador (email, password, telefone) VALUES ('" + email + "','" + password + "','" + telefone + "')";
                int rowsInserted = stmt.executeUpdate(sql);

                sendHeartbeat(conn, sql);
                return rowsInserted > 0;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir utilizador na base de dados: " + e.getMessage());
            return false;
        }

    }

    private static void startHeartBeat(int listeningPort, String dbPath) {

        Timer timer = new Timer(true); // "true" para rodar como daemon

        TimerTask heartbeatTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    String message = "NÃO HOUVE ATUALIZAÇÃO";
                    // Envia a mensagem de heartbeat
                    sendHeartbeat(null, message);
                } catch (IOException e) {
                    System.out.println("Erro ao enviar heartbeat: " + e.getMessage());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        timer.scheduleAtFixedRate(heartbeatTask, 0, HEARTBEAT_INTERVAL);
    }

    private static void sendHeartbeat(Connection database, String lastQuery) throws IOException, SQLException {
        InetAddress group = InetAddress.getByName(HEARTBEAT_ADDRESS);


        String heartbeatMessage = "VERSION:" + /*getVersion(dbPath)*/ currentVersion + ";QUERY:" + lastQuery + ";PORT:5000";
        try (MulticastSocket multicastSocket = new MulticastSocket(HEARTBEAT_PORT)) {
            multicastSocket.joinGroup(group);
            byte[] buffer = heartbeatMessage.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, HEARTBEAT_PORT);
            multicastSocket.send(packet);
            System.out.println("Heartbeat enviado: " + heartbeatMessage);
        }
        if (!lastQuery.equals("NÃO HOUVE ATUALIZAÇÃO"))
            upVersionDB(database);
    }

    public static boolean grupoExiste(String groupName, String dbFilePath) {
        String url = "jdbc:sqlite:" + dbFilePath;

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {

            String sql = "SELECT COUNT(*) FROM grupo WHERE nome = '" + groupName + "'";
            stmt.execute(sql);
            ResultSet resultSet = stmt.getResultSet();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true;  // Grupo já existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // Grupo não existe
    }

    public static boolean criarGrupo(String groupName, String creatorEmail, String dbFilePath) {
        String url = "jdbc:sqlite:" + dbFilePath;

        try (Connection connection = DriverManager.getConnection(url)) {
            connection.setAutoCommit(false);  // Usar transações

            try (Statement insertGroupStmt = connection.createStatement();
                 Statement getUserIdStmt = connection.createStatement();
                 Statement getGroupIdStmt = connection.createStatement();
                 Statement addMemberStmt = connection.createStatement()) {

                // Inserir o grupo
                String sqlInsertGroup = "INSERT INTO grupo(nome) VALUES('" + groupName + "')";
                insertGroupStmt.executeUpdate(sqlInsertGroup);

                sendHeartbeat(connection, sqlInsertGroup);


                // Obter o ID do utilizador que criou o grupo
                String sqlGetUserId = "SELECT id_utilizador FROM utilizador WHERE email = '" + creatorEmail + "'";
                getUserIdStmt.execute(sqlGetUserId);
                ResultSet userIdResult = getUserIdStmt.getResultSet();
                if (!userIdResult.next()) {
                    connection.rollback();  // Se não encontrar o utilizador, desfaz a transação
                    return false;
                }

                int userId = userIdResult.getInt("id_utilizador");

                // Obter o ID do grupo recém-criado
                String sqlGetGroupId = "SELECT id_grupo FROM grupo WHERE nome = '" + groupName + "'";
                getGroupIdStmt.execute(sqlGetGroupId);
                ResultSet groupIdResult = getGroupIdStmt.getResultSet();
                if (!groupIdResult.next()) {
                    connection.rollback();  // Se não encontrar o grupo, desfaz a transação
                    return false;
                }
                int groupId = groupIdResult.getInt("id_grupo");

                String sqlAddMember = "INSERT INTO grupo_utilizador(id_grupo, id_utilizador) VALUES(" + groupId + ", " + userId + ")";
                addMemberStmt.executeUpdate(sqlAddMember);
                sendHeartbeat(connection, sqlAddMember);
                connection.commit();  // Confirma a transação
                return true;
            } catch (SQLException e) {
                connection.rollback();  // Desfaz a transação em caso de erro
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void imprimeGrupos(String username, String dbFilePath, PrintWriter out) {
        String url = "jdbc:sqlite:" + dbFilePath;

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {

            String sql = "SELECT g.nome FROM grupo g INNER JOIN grupo_utilizador gu ON g.id_grupo = gu.id_grupo INNER JOIN utilizador u ON gu.id_utilizador = u.id_utilizador WHERE u.email = '" + username + "'";
            stmt.execute(sql);

            // Executar a consulta
            ResultSet resultSet = stmt.getResultSet();

            // Verificar se o utilizador pertence a algum grupo
            boolean hasGroups = false;
            while (resultSet.next()) {
                hasGroups = true;
                String groupName = resultSet.getString("nome");
                out.println("Grupo: " + groupName);  // Enviar o nome do grupo para o cliente
            }

            if (!hasGroups) {
                out.println("Não pertence a nenhum grupo.");
            }

            // Enviar "FIM" para indicar o término da lista
            out.println("FIM");

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Erro ao recuperar grupos.");
            out.println("FIM");  // Enviar "FIM" mesmo em caso de erro
        }
    }

    public static boolean enviarConvite(String usernameConvidado, String nomeGrupo, String dbFilePath) {
        String url = "jdbc:sqlite:" + dbFilePath;

        try (Connection connection = DriverManager.getConnection(url);
             Statement getGroupIdStmt = connection.createStatement();
             Statement getUserIdStmt = connection.createStatement();
             Statement insertConviteStmt = connection.createStatement()) {

            String sqlGetGroupId = "SELECT id_grupo FROM grupo WHERE nome = '" + nomeGrupo + "'";
            getGroupIdStmt.execute(sqlGetGroupId);
            ResultSet groupResultSet = getGroupIdStmt.getResultSet();
            if (!groupResultSet.next()) {
                return false;
            }
            int groupId = groupResultSet.getInt("id_grupo");

            String sqlGetUserId = "SELECT id_utilizador FROM utilizador WHERE email = '" + usernameConvidado + "'";
            getUserIdStmt.execute(sqlGetUserId);
            ResultSet userResultSet = getUserIdStmt.getResultSet();
            if (!userResultSet.next()) {
                return false;  //Não encontrado o utilizador
            }
            int userId = userResultSet.getInt("id_utilizador");

            // Criar o convite
            String sqlInsertConvite = "INSERT INTO convite(id_grupo, id_utilizador_convidado, estado) VALUES(" + groupId + "," + userId + ", 'pendente')";
            insertConviteStmt.executeUpdate(sqlInsertConvite);
            sendHeartbeat(connection, sqlInsertConvite);

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> listarConvitesPendentes(String username, String dbFilePath) {
        String url = "jdbc:sqlite:" + dbFilePath;

        List<String> gruposPendentes = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url);
             Statement getUserIdStmt = connection.createStatement();
             Statement getConvitesStmt = connection.createStatement()) {

            // Obter o ID do utilizador
            String sqlGetUserId = "SELECT id_utilizador FROM utilizador WHERE email = '" + username + "'";
            getUserIdStmt.execute(sqlGetUserId);
            ResultSet userResultSet = getUserIdStmt.getResultSet();
            if (!userResultSet.next()) {
                return gruposPendentes;  // Utilizador não encontrado
            }
            int userId = userResultSet.getInt("id_utilizador");

            // Obter convites pendentes
            String sqlGetConvites = "SELECT g.nome FROM convite c JOIN grupo g ON c.id_grupo = g.id_grupo WHERE c.id_utilizador_convidado = " + userId + " AND c.estado = 'pendente'";
            getConvitesStmt.execute(sqlGetConvites);
            ResultSet convitesResultSet = getConvitesStmt.getResultSet();

            // Preencher a lista com os nomes dos grupos
            while (convitesResultSet.next()) {
                gruposPendentes.add(convitesResultSet.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gruposPendentes;
    }

    public static boolean aceitarConvite(String username, String nomeGrupo, String dbFilePath) {
        String url = "jdbc:sqlite:" + dbFilePath;

        try (Connection connection = DriverManager.getConnection(url);
             Statement getGroupIdStmt = connection.createStatement();
             Statement getUserIdStmt = connection.createStatement();
             Statement updateConviteStmt = connection.createStatement();
             Statement addUserToGroupStmt = connection.createStatement()) {

            // Obter o ID do grupo
            String sqlGetGroupId = "SELECT id_grupo FROM grupo WHERE nome = '" + nomeGrupo + "'";
            ResultSet groupResultSet = getGroupIdStmt.executeQuery(sqlGetGroupId);
            if (!groupResultSet.next()) {
                return false;  // Grupo não encontrado
            }
            int groupId = groupResultSet.getInt("id_grupo");

            // Obter o ID do utilizador
            String sqlGetUserId = "SELECT id_utilizador FROM utilizador WHERE email = '" + username + "'";
            ResultSet userResultSet = getUserIdStmt.executeQuery(sqlGetUserId);
            if (!userResultSet.next()) {
                return false;  // Utilizador não encontrado
            }
            int userId = userResultSet.getInt("id_utilizador");

            // Atualizar o estado do convite para "aceite"
            String sqlUpdateConvite = "UPDATE convite SET estado = 'aceite ' WHERE id_grupo = " + groupId + " AND id_utilizador_convidado = " + userId + " AND estado = 'pendente'";
            int rowsAffected = updateConviteStmt.executeUpdate(sqlUpdateConvite);

            sendHeartbeat(connection, sqlUpdateConvite);

            if (rowsAffected == 0) {
                return false;  // Nenhum convite pendente encontrado ou erro ao aceitar
            }

            // Adicionar o utilizador ao grupo
            String sqlAddUserToGroup = "INSERT INTO grupo_utilizador (id_grupo, id_utilizador) VALUES (" + groupId + ", " + userId + ")";
            addUserToGroupStmt.executeUpdate(sqlAddUserToGroup);
            sendHeartbeat(connection, sqlAddUserToGroup);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean editarNomeGrupo(String username, String nomeAtualGrupo, String novoNomeGrupo, String dbFilePath) {
        String url = "jdbc:sqlite:" + dbFilePath;

        try (Connection connection = DriverManager.getConnection(url);
             Statement verificarMembroStmt = connection.createStatement();
             Statement atualizarNomeGrupoStmt = connection.createStatement()) {

            // Verificar se o utilizador é membro do grupo
            String sqlVerificarMembro = "SELECT gu.id_grupo FROM grupo_utilizador gu JOIN utilizador u ON gu.id_utilizador = u.id_utilizador JOIN grupo g ON gu.id_grupo = g.id_grupo WHERE u.email = '" + username + "' AND g.nome = '" + nomeAtualGrupo + "'";
            ResultSet resultSet = verificarMembroStmt.executeQuery(sqlVerificarMembro);

            if (!resultSet.next()) {
                return false;  // Utilizador não é membro do grupo
            }

            int grupoId = resultSet.getInt("id_grupo");

            // Atualizar o nome do grupo
            String sqlAtualizarNomeGrupo = "UPDATE grupo SET nome = '" + novoNomeGrupo + "' WHERE id_grupo = '" + grupoId + "'";
            atualizarNomeGrupoStmt.executeUpdate(sqlAtualizarNomeGrupo);
            sendHeartbeat(connection, sqlAtualizarNomeGrupo);

            return true;  // Nome do grupo alterado com sucesso
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean inserirDespesa(String usernameInseriu, String nomeGrupo, String descricao, double valor, List<String> participantes, String data, String dbFilePath) {
        String url = "jdbc:sqlite:" + dbFilePath;

        try (Connection connection = DriverManager.getConnection(url);
             Statement obterIdGrupoStmt = connection.createStatement();
             Statement inserirDespesaStmt = connection.createStatement();
             Statement inserirParticipacaoStmt = connection.createStatement();
             Statement obterIdUtilizadorStmt = connection.createStatement()) {

            // Obter ID do grupo
            String sqlObterIdGrupo = "SELECT gu.id_grupo FROM grupo_utilizador gu JOIN utilizador u ON gu.id_utilizador = u.id_utilizador JOIN grupo g ON gu.id_grupo = g.id_grupo WHERE u.email = '" + usernameInseriu + "' AND g.nome = '" + nomeGrupo + "'";
            ResultSet resultSetGrupo = obterIdGrupoStmt.executeQuery(sqlObterIdGrupo);
            if (!resultSetGrupo.next()) {
                System.out.println("Erro: O utilizador não pertence ao grupo especificado.");
                return false;
            }
            int grupoId = resultSetGrupo.getInt("id_grupo");

            // Obter ID do utilizador que inseriu a despesa
            String sqlObterIdUtilizador = "SELECT id_utilizador FROM utilizador WHERE email = '" + usernameInseriu + "'";
            ResultSet resultSetUtilizador = obterIdUtilizadorStmt.executeQuery(sqlObterIdUtilizador);
            if (!resultSetUtilizador.next()) {
                System.out.println("Erro: Utilizador que inseriu a despesa não encontrado.");
                return false;
            }
            int idInseriu = resultSetUtilizador.getInt("id_utilizador");

            // Inserir despesa na tabela `despesa`
            String sqlInserirDespesa = "INSERT INTO despesa (descricao, valor, id_grupo, data, id_utilizador) VALUES ('" + descricao + "', " + valor + ", " + grupoId + ", '" + data + "', " + idInseriu + ")";
            inserirDespesaStmt.executeUpdate(sqlInserirDespesa);
            sendHeartbeat(connection, sqlInserirDespesa);

            ResultSet generatedKeys = inserirDespesaStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                System.out.println("Erro: Não foi possível obter o ID da despesa.");
                return false;
            }
            int despesaId = generatedKeys.getInt(1);
            System.out.println("Despesa inserida com sucesso com ID: " + despesaId);

            // Calcular o valor de participação para cada utilizador
            double valorParticipacao = valor / participantes.size();

            // Inserir cada participante na tabela `despesa_utilizador`
            for (String participante : participantes) {
                sqlObterIdUtilizador = "SELECT id_utilizador FROM utilizador WHERE email = '" + participante + "'";
                ResultSet resultSetParticipante = obterIdUtilizadorStmt.executeQuery(sqlObterIdUtilizador);
                if (resultSetParticipante.next()) {
                    int idParticipante = resultSetParticipante.getInt(1);

                    String sqlInserirParticipacao = "INSERT INTO despesa_utilizador (id_despesa, id_utilizador, valor_participacao) VALUES (" + despesaId + ", " + idParticipante + ", " + valorParticipacao + ")";
                    inserirParticipacaoStmt.executeUpdate(sqlInserirParticipacao);
                    System.out.println("Participação inserida para utilizador: " + participante);
                } else {
                    System.out.println("Erro: Participante " + participante + " não encontrado.");
                }
            }

            return true;
        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean eliminarGrupoPorNome(String nomeGrupo, String dbFilePath) {

        String url = "jdbc:sqlite:" + dbFilePath;

        try (Connection connection = DriverManager.getConnection(url);
             Statement verificaDespesasStmt = connection.createStatement();
             Statement verificaDividasStmt = connection.createStatement();
             Statement eliminaStmt = connection.createStatement()) {

            String sqlVerificaDespesas = "SELECT COUNT(*) as count FROM despesa WHERE id_grupo = (SELECT id_grupo FROM grupo WHERE nome = '" + nomeGrupo + "') ";
            ResultSet resultSet = verificaDespesasStmt.executeQuery(sqlVerificaDespesas);
            if (resultSet.next() && resultSet.getInt("count") > 0) {

                return false;
            }
            String sqlVerificaDividas = "SELECT COUNT(*) as count FROM despesa_utilizador WHERE id_despesa IN ( SELECT id_despesa FROM despesa WHERE id_grupo = (SELECT id_grupo FROM grupo WHERE nome = '" + nomeGrupo + "')) AND valor_participacao > 0 ";
            ResultSet resultSetDividas = verificaDividasStmt.executeQuery(sqlVerificaDividas);
            if (resultSetDividas.next() && resultSetDividas.getInt("count") > 0) {
                return false;
            }

            String sqlEliminarGrupo = "DELETE FROM grupo WHERE nome = '" + nomeGrupo + "'";
            eliminaStmt.executeUpdate(sqlEliminarGrupo);
            sendHeartbeat(connection, sqlEliminarGrupo);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean sairDoGrupo(String nomeGrupo, String username, String dbFilePath) {
        String url = "jdbc:sqlite:" + dbFilePath;

        try (Connection connection = DriverManager.getConnection(url);
             Statement verificaDespesasStmt = connection.createStatement();
             Statement saiStmt = connection.createStatement()) {

            // Verifica se existem despesas associadas ao utilizador no grupo
            String sqlVerificaDespesas = "SELECT COUNT(*) as count FROM despesa WHERE id_grupo = (SELECT id_grupo FROM grupo WHERE nome = '" + nomeGrupo + "') AND id_utilizador = (SELECT id_utilizador FROM utilizador WHERE email = '" + username + "') ";
            ResultSet resultSet = verificaDespesasStmt.executeQuery(sqlVerificaDespesas);

            if (resultSet.next() && resultSet.getInt("count") > 0) {
                // Existem despesas associadas ao utilizador no grupo
                return false;
            }

            // Se não houver despesas, sai do grupo
            String sqlSairDoGrupo = "DELETE FROM grupo_utilizador WHERE id_grupo = (SELECT id_grupo FROM grupo WHERE nome = '" + nomeGrupo + "') AND id_utilizador = (SELECT id_utilizador FROM utilizador WHERE email = '" + username + "')";
            int rowsAffected = saiStmt.executeUpdate(sqlSairDoGrupo);
            sendHeartbeat(connection, sqlSairDoGrupo);

            // Retorna true se o utilizador saiu com sucesso do grupo
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao sair do grupo: " + e.getMessage());
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static double obterTotalGastos(String nomeGrupo, String username, String dbFilePath) {
        String url = "jdbc:sqlite:" + dbFilePath;

        try (Connection connection = DriverManager.getConnection(url);
             Statement obterIdGrupoStmt = connection.createStatement();
             Statement calcularTotalGastosStmt = connection.createStatement()) {

            // Obter ID do grupo
            // SQL para obter o ID do grupo com base no nome do grupo e no utilizador
            String sqlObterIdGrupo = "SELECT gu.id_grupo FROM grupo_utilizador gu JOIN utilizador u ON gu.id_utilizador = u.id_utilizador JOIN grupo g ON gu.id_grupo = g.id_grupo WHERE u.email = '" + username + "' AND g.nome = '" + nomeGrupo + "'";
            ResultSet resultSetGrupo = obterIdGrupoStmt.executeQuery(sqlObterIdGrupo);

            if (!resultSetGrupo.next()) {
                return -1;  // O utilizador não pertence ao grupo
            }

            int grupoId = resultSetGrupo.getInt("id_grupo");

            // Calcular total de gastos
            // SQL para calcular o total de despesas para o grupo
            String sqlCalcularTotalGastos = "SELECT SUM(valor) AS total_gastos FROM despesa WHERE id_grupo = '" + grupoId + "'";
            ResultSet resultSetTotal = calcularTotalGastosStmt.executeQuery(sqlCalcularTotalGastos);

            if (resultSetTotal.next()) {
                return resultSetTotal.getDouble("total_gastos");
            } else {
                return 0;  // Não há despesas registradas
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;  // Erro ao acessar a base de dados
        }
    }

    public static List<String> obterHistoricoDespesas(String nomeGrupo, String username, String dbFilePath) {
        String url = "jdbc:sqlite:" + dbFilePath;

        List<String> historico = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url);
             Statement obterIdGrupoStmt = connection.createStatement();
             Statement historicoDespesasStmt = connection.createStatement()) {

            // Obter ID do grupo
            // SQL para obter o ID do grupo com base no nome do grupo e no utilizador
            String sqlObterIdGrupo = "SELECT gu.id_grupo FROM grupo_utilizador gu JOIN utilizador u ON gu.id_utilizador = u.id_utilizador JOIN grupo g ON gu.id_grupo = g.id_grupo WHERE u.email = '" + username + "' AND g.nome = '" + nomeGrupo + "'";
            ResultSet resultSetGrupo = obterIdGrupoStmt.executeQuery(sqlObterIdGrupo);

            if (!resultSetGrupo.next()) {
                return null;  // O utilizador não pertence ao grupo
            }

            int grupoId = resultSetGrupo.getInt("id_grupo");

            // Obter histórico de despesas
            // SQL para obter o histórico de despesas
            String sqlHistoricoDespesas = "SELECT d.descricao, d.valor, d.data, u.email AS inserido_por FROM despesa d JOIN utilizador u ON d.id_utilizador = u.id_utilizador WHERE d.id_grupo = '" + grupoId + "' ORDER BY d.data ASC";
            ResultSet resultSetHistorico = historicoDespesasStmt.executeQuery(sqlHistoricoDespesas);

            while (resultSetHistorico.next()) {
                String descricao = resultSetHistorico.getString("descricao");
                double valor = resultSetHistorico.getDouble("valor");
                String data = resultSetHistorico.getString("data");
                String inseridoPor = resultSetHistorico.getString("inserido_por");

                String detalhe = String.format("Descrição: %s, Valor: %.2f, Data: %s, Inserido por: %s",
                        descricao, valor, data, inseridoPor);
                historico.add(detalhe);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;  // Erro ao acessar a base de dados
        }

        return historico;
    }

    public static List<String> obterDividas(String nomeGrupo, String username, String dbFilePath) {
        List<String> dividas = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath)) {
            String sql = "SELECT d.id_despesa, d.descricao, d.valor AS valor_total, d.data, uCriador.email AS criador, du.valor_participacao FROM despesa d JOIN despesa_utilizador du ON d.id_despesa = du.id_despesa JOIN utilizador uCriador ON d.id_utilizador = uCriador.id_utilizador JOIN grupo g ON d.id_grupo = g.id_grupo WHERE g.nome = '" + nomeGrupo + "' AND du.id_utilizador = (SELECT id_utilizador FROM utilizador WHERE email = '" + username + "')";

            try (Statement stmt = conn.createStatement()) {

                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    dividas.add("ID Despesa: " + rs.getInt("id_despesa") + " | Criador: " + rs.getString("criador") +
                            " | Valor Total: " + rs.getDouble("valor_total") + " | Valor Devido: " + rs.getDouble("valor_participacao") +
                            " | Data: " + rs.getString("data") + " | Descrição: " + rs.getString("descricao"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dividas;
    }

    public static String efetuarPagamento(String username, int idDivida, double valorPagamento, String dbFilePath) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath)) {
            String sqlVerificarDivida = """
                    SELECT d.id_utilizador AS id_utilizador_criador, du.valor_participacao 
                    FROM despesa d 
                    JOIN despesa_utilizador du ON d.id_despesa = du.id_despesa 
                    WHERE d.id_despesa = ? 
                    AND du.id_utilizador = (SELECT id_utilizador FROM utilizador WHERE email = ?)
                    """;

            try (PreparedStatement stmtVerificar = conn.prepareStatement(sqlVerificarDivida)) {
                stmtVerificar.setInt(1, idDivida);
                stmtVerificar.setString(2, username);

                ResultSet rs = stmtVerificar.executeQuery();

                if (rs.next()) {
                    int idUtilizadorRecebedor = rs.getInt("id_utilizador_criador"); // ID do criador da dívida
                    double valorDivida = rs.getDouble("valor_participacao");

                    if (valorPagamento<=0 || Double.isNaN(valorPagamento))
                        return "Valor pagamento não será aceite";

                    if (valorPagamento > valorDivida) {
                        return "Valor do pagamento excede a dívida.";
                    }

                    // Inserir na tabela de pagamento
                    String sqlPagamento = "INSERT INTO pagamento (valor, id_utilizador_pagador, id_utilizador_recebedor, id_despesa, data) VALUES ("+valorPagamento+", (SELECT id_utilizador FROM utilizador WHERE email = '"+username+"'), "+idUtilizadorRecebedor+", "+idDivida+", CURRENT_DATE) ";

                    try (Statement stmtPagamento = conn.createStatement()) {
                        stmtPagamento.executeUpdate(sqlPagamento);

                        // Enviar heartbeat
                        sendHeartbeat(conn, sqlPagamento);
                    }

                    if (valorPagamento < valorDivida) {
                        // Atualizar a dívida restante
                        double valorRestante = valorDivida - valorPagamento;
                        String sqlAtualizarDivida = "UPDATE despesa_utilizador SET valor_participacao = "+valorRestante+" WHERE id_despesa = "+idDivida+" AND id_utilizador = (SELECT id_utilizador FROM utilizador WHERE email = '"+username+"') ";

                        try (Statement stmtAtualizar = conn.createStatement()) {
                            stmtAtualizar.executeUpdate(sqlAtualizarDivida);

                            // Enviar heartbeat
                            sendHeartbeat(conn, sqlAtualizarDivida);
                        }
                    }

                    if (valorPagamento == valorDivida) {
                        // Remover a dívida da tabela despesa_utilizador
                        String sqlRemoverDivida = "DELETE FROM despesa_utilizador WHERE id_despesa = "+idDivida+" AND id_utilizador = (SELECT id_utilizador FROM utilizador WHERE email = '"+username+"') ";

                        try (Statement stmtRemover = conn.createStatement()) {
                            stmtRemover.executeUpdate(sqlRemoverDivida);

                            // Enviar heartbeat
                            sendHeartbeat(conn, sqlRemoverDivida);
                        }
                    }

                    return "Pagamento efetuado com sucesso.";
                } else {
                    return "Dívida não encontrada ou já paga.";
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            return "Erro ao efetuar pagamento.";
        }
    }


    public static Map<String, Object> visualizarSaldos(String nomeGrupo, String dbFilePath) {
        Map<String, Object> saldos = new HashMap<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath)) {
            // Obter id do grupo
            String sqlGrupo = "SELECT id_grupo FROM grupo WHERE nome = '" + nomeGrupo + "'";
            Statement stmtGrupo = conn.createStatement();
            ResultSet rsGrupo = stmtGrupo.executeQuery(sqlGrupo);

            if (rsGrupo.next()) {
                int idGrupo = rsGrupo.getInt("id_grupo");

                // Obter utilizadores do grupo
                String sqlUtilizadores = "SELECT u.id_utilizador, u.email FROM utilizador u JOIN grupo_utilizador gu ON u.id_utilizador = gu.id_utilizador WHERE gu.id_grupo = " + idGrupo;
                Statement stmtUtilizadores = conn.createStatement();
                ResultSet rsUtilizadores = stmtUtilizadores.executeQuery(sqlUtilizadores);

                while (rsUtilizadores.next()) {
                    int idUtilizador = rsUtilizadores.getInt("id_utilizador");
                    String emailUtilizador = rsUtilizadores.getString("email");


                    double gastoTotal = 0;
                    double totalDeve = 0;
                    double totalReceber = 0;
                    Map<String, Double> dividasPorUtilizador = new HashMap<>();
                    Map<String, Double> recebimentosPorUtilizador = new HashMap<>();

                    // Obter despesas criadas pelo utilizador
                    String sqlGastos = "SELECT SUM(d.valor) as gasto_total FROM despesa d WHERE d.id_utilizador = " + idUtilizador + " AND d.id_grupo = " + idGrupo;
                    Statement stmtGastos = conn.createStatement();
                    ResultSet rsGastos = stmtGastos.executeQuery(sqlGastos);
                    if (rsGastos.next()) {
                        gastoTotal = rsGastos.getDouble("gasto_total");
                    }

                    // Obter total que deve a outros utilizadores
                    String sqlDividas = "SELECT u.email, du.valor_participacao FROM despesa_utilizador du JOIN despesa d ON du.id_despesa = d.id_despesa JOIN utilizador u ON d.id_utilizador = u.id_utilizador WHERE d.id_grupo = " + idGrupo + " AND du.id_utilizador = " + idUtilizador;
                    Statement stmtDividas = conn.createStatement();
                    ResultSet rsDividas = stmtDividas.executeQuery(sqlDividas);
                    while (rsDividas.next()) {
                        String emailCredor = rsDividas.getString("email");
                        double valorDivida = rsDividas.getDouble("valor_participacao");
                        totalDeve += valorDivida;

                        dividasPorUtilizador.put(emailCredor, valorDivida);
                    }


                    String sqlRecebimentos = "SELECT u.email, du.valor_participacao FROM despesa_utilizador du JOIN despesa d ON du.id_despesa = d.id_despesa JOIN utilizador u ON du.id_utilizador = u.id_utilizador WHERE d.id_grupo = " + idGrupo + " AND d.id_utilizador = " + idUtilizador;
                    Statement stmtRecebimentos = conn.createStatement();
                    ResultSet rsRecebimentos = stmtRecebimentos.executeQuery(sqlRecebimentos);
                    while (rsRecebimentos.next()) {
                        String emailDevedor = rsRecebimentos.getString("email");
                        double valorRecebimento = rsRecebimentos.getDouble("valor_participacao");
                        totalReceber += valorRecebimento;

                        recebimentosPorUtilizador.put(emailDevedor, valorRecebimento);
                    }

                    // Adicionar informações ao mapa de saldos
                    Map<String, Object> detalhes = new HashMap<>();
                    detalhes.put("gasto_total", gastoTotal);
                    detalhes.put("total_deve", totalDeve);
                    detalhes.put("total_receber", totalReceber);
                    detalhes.put("dividas_por_utilizador", dividasPorUtilizador);
                    detalhes.put("recebimentos_por_utilizador", recebimentosPorUtilizador);

                    saldos.put(emailUtilizador, detalhes);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return saldos;
    }

    public static String eliminarDespesa(int idDespesa, String dbFilePath) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath)) {
            // Primeiro, vamos obter o ID da despesa com a descrição fornecida
            String sqlGetId = "SELECT id_despesa FROM despesa WHERE id_despesa = " + idDespesa;
            try (Statement pstmt = conn.createStatement()) {
                ResultSet rs = pstmt.executeQuery(sqlGetId);

                if (rs.next()) {

                    // Excluir da tabela despesa_utilizador
                    String sqlDeleteUtilizador = "DELETE FROM despesa_utilizador WHERE id_despesa = '" + idDespesa + "'";
                    try (Statement stmtDelete = conn.createStatement()) {
                        stmtDelete.executeUpdate(sqlDeleteUtilizador);
                        sendHeartbeat(conn, sqlDeleteUtilizador);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Excluir da tabela despesa
                    String sqlDeleteDespesa = "DELETE FROM despesa WHERE id_despesa = " + idDespesa;
                    try (Statement stmtDeleteDespesa = conn.createStatement()) {
                        stmtDeleteDespesa.executeUpdate(sqlDeleteDespesa);
                        sendHeartbeat(conn, sqlDeleteDespesa);
                    }

                    return "Despesa '" + idDespesa + "' excluída com sucesso.";
                } else {
                    return "Despesa não encontrada.";
                }
            }
        } catch (SQLException | IOException e) {
            return "Erro ao excluir despesa: " + e.getMessage();
        }
    }

    public static String editarDespesa(int idDespesa, String novaDescricao, double novoValor, String novaData, String dbFilePath) throws IOException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
             Statement stmt = conn.createStatement()) {

            // Obter o ID da despesa com a descrição fornecida
            String sqlGetId = "SELECT id_despesa FROM despesa WHERE id_despesa = " + idDespesa;
            ResultSet rs = stmt.executeQuery(sqlGetId);

            if (rs.next()) {

                // Atualizar a despesa na tabela despesa
                String sqlUpdateDespesa = "UPDATE despesa SET descricao = '" + novaDescricao + "', valor = " + novoValor + ", data = '" + novaData + "' WHERE id_despesa = " + idDespesa;
                stmt.executeUpdate(sqlUpdateDespesa);
                sendHeartbeat(conn, sqlUpdateDespesa);

                // Contar os participantes associados à despesa
                String sqlContarUtilizadores = "SELECT COUNT(id_utilizador) AS total_utilizadores FROM despesa_utilizador WHERE id_despesa = " + idDespesa;
                ResultSet resultSet = stmt.executeQuery(sqlContarUtilizadores);

                int participantes = 0;
                if (resultSet.next()) {
                    participantes = resultSet.getInt("total_utilizadores");
                }

                if (participantes <= 0) {
                    return "Despesa não tem participantes.";
                }

                // Atualizar o valor de participação na tabela despesa_utilizador
                String sqlUpdateValorParticipacao = "UPDATE despesa_utilizador SET valor_participacao = " + (novoValor / participantes) + " WHERE id_despesa = " + idDespesa;
                stmt.executeUpdate(sqlUpdateValorParticipacao);
                sendHeartbeat(conn, sqlUpdateValorParticipacao);

                return "Despesa '" + idDespesa + "' editada com sucesso.";
            } else {
                return "Despesa não encontrada.";
            }
        } catch (SQLException e) {
            return "Erro ao editar despesa: " + e.getMessage();
        }
    }



    public static void main(String[] args) throws IOException {

        int listeningPort;
        String dbFilePath;
        File dbFile;

        if (args.length != 2) {
            System.out.println("Sintaxe: java Servidor [port] [fileDataBaseName]");
            return;
        }

        dbFilePath = args[1].trim();
        dbFile = new File(dbFilePath);

        if (!dbFile.exists()) {
            System.out.println("A diretoria " + dbFile + " não existe!");
            criaNovaBaseDados(dbFilePath);
        }
        if (!dbFile.canRead()) {
            System.out.println("Sem permissões de leitura na diretoria " + dbFile + "!");
            return;
        }

        try {

            listeningPort = Integer.parseInt(args[0]);

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath)) {
                System.out.println("Conectado à base de dados SQLite com sucesso.");

                startHeartBeat(listeningPort, dbFilePath);

                try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {

                    System.out.println("Servidor iniciado na porta " + listeningPort + ". Aguardando conexões...");

                    while (true) {
                        try {
                            Socket socket = serverSocket.accept();

                            socket.setSoTimeout(TIMEOUT);

                            System.out.println("Cliente conectado: " + socket.getInetAddress());

                            ClientHandler clientHandler = new ClientHandler(socket, connection, dbFilePath);
                            Thread clientThread = new Thread(clientHandler);
                            clientThread.start();

                        } catch (SocketTimeoutException ex) {
                            System.out.println("O cliente não enviou qualquer dado (timeout).");
                        } catch (IOException ex) {
                            System.out.println("Problema de I/O ao atender o cliente: " + ex.getMessage());
                        }
                    }

                } catch (SocketException e) {
                    System.out.println("Ocorreu uma exceção no socket: " + e.getMessage());
                } catch (IOException e) {
                    System.out.println("Ocorreu um erro de E/S: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println("Erro ao conectar à base de dados SQLite: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um número inteiro válido: " + e.getMessage());
        }
    }

    public static void handleGetDBRequest(Socket socket, String dbFilePath) {
        System.out.println("Solicitação de backup recebida.");

        try (OutputStream out = socket.getOutputStream();
             InputStream fis = new FileInputStream(dbFilePath)) {

            // Envia a base de dados para o cliente (Backup Server)
            byte[] buffer = new byte[4096];
            int bytesRead;
            int n_bytes_lidos = 0;

            while ((bytesRead = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead); // Envia os dados lidos
                System.out.println("Dados enviados...");
                n_bytes_lidos += bytesRead;
            }

            System.out.println("arquivo com " + n_bytes_lidos + " enviados");


            out.flush(); // Garante que todos os dados foram enviados
            System.out.println("Cópia da base de dados enviada ao servidor de backup.");
        } catch (IOException e) {
            System.err.println("Erro ao processar solicitação de backup.");
            e.printStackTrace();
        }
    }

    public static boolean exportarDespesasCSV(String nomeGrupo, String caminhoCSV, String username, String dbFilePath) {
        String url = "jdbc:sqlite:" + dbFilePath;

        // Consulta para obter informações gerais do grupo e participantes
        String queryParticipantes = """
                SELECT u.email
                FROM grupo_utilizador gu
                INNER JOIN grupo g ON gu.id_grupo = g.id_grupo
                INNER JOIN utilizador u ON gu.id_utilizador = u.id_utilizador
                WHERE g.nome = ?;
                """;

        // Consulta para obter as despesas do grupo no formato necessário
        String queryDespesas = """
                SELECT d.data, u.email AS responsavel, d.valor, 
                       (SELECT email FROM utilizador WHERE id_utilizador = dp.id_utilizador_pagador) AS pagador,
                       GROUP_CONCAT((SELECT email FROM utilizador WHERE id_utilizador = du.id_utilizador), ';') AS participantes
                FROM despesa d
                INNER JOIN utilizador u ON d.id_utilizador = u.id_utilizador
                INNER JOIN grupo g ON d.id_grupo = g.id_grupo
                LEFT JOIN pagamento dp ON d.id_despesa = dp.id_despesa
                LEFT JOIN despesa_utilizador du ON d.id_despesa = du.id_despesa
                WHERE g.nome = ?
                GROUP BY d.id_despesa;
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmtParticipantes = conn.prepareStatement(queryParticipantes);
             PreparedStatement stmtDespesas = conn.prepareStatement(queryDespesas);
             FileWriter writer = new FileWriter(caminhoCSV)) {

            // Obter participantes
            stmtParticipantes.setString(1, nomeGrupo);
            ResultSet rsParticipantes = stmtParticipantes.executeQuery();
            List<String> participantes = new ArrayList<>();
            while (rsParticipantes.next()) {
                participantes.add(rsParticipantes.getString("email"));
            }

            // Escrever cabeçalho geral
            writer.append("\"Nome do grupo\"\n");
            writer.append("\"").append(nomeGrupo).append("\"\n");
            writer.append("\"Elementos\"\n");
            writer.append(String.join(";", participantes)).append("\n");
            writer.append("\"Data\";\"Responsável pelo registo da despesa\";\"Valor\";\"Pago por\";\"A dividir com\"\n");

            // Obter despesas
            stmtDespesas.setString(1, nomeGrupo);
            ResultSet rsDespesas = stmtDespesas.executeQuery();
            while (rsDespesas.next()) {
                String data = rsDespesas.getString("data");
                String responsavel = rsDespesas.getString("responsavel");
                double valor = rsDespesas.getDouble("valor");
                String pagador = rsDespesas.getString("pagador");
                String aDividirCom = rsDespesas.getString("participantes");

                writer.append("\"").append(data).append("\";")
                        .append("\"").append(responsavel).append("\";")
                        .append("\"").append(String.valueOf(valor)).append("\";")
                        .append("\"").append(pagador).append("\";")
                        .append("\"").append(aDividirCom != null ? aDividirCom : "").append("\"\n");
            }

            System.out.println("Despesas exportadas com sucesso!");
            return true;

        } catch (Exception e) {
            System.err.println("Erro ao exportar despesas: " + e.getMessage());
            return false;
        }
    }

    public static String listarPagamentos(String nomeGrupo, String dbFilePath) {
        StringBuilder pagamentos = new StringBuilder();

        // Query atualizada: Seleciona todos os campos de pagamento onde os id_utilizadores estão associados ao grupo
        String query = String.format("""
            SELECT p.id_pagamento, p.valor, p.data, p.id_utilizador_pagador, p.id_utilizador_recebedor
            FROM pagamento p
            JOIN despesa d ON p.id_despesa = d.id_despesa
            JOIN grupo_utilizador gu ON gu.id_grupo = d.id_grupo
            WHERE gu.id_grupo = (SELECT id_grupo FROM grupo WHERE nome = '%s') 
            AND (gu.id_utilizador = p.id_utilizador_pagador OR gu.id_utilizador = p.id_utilizador_recebedor)
            """, nomeGrupo);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Se houver pagamentos, itere sobre eles
            while (rs.next()) {
                int idPagamento = rs.getInt("id_pagamento");
                double valor = rs.getDouble("valor");
                String data = rs.getString("data");
                int idPagador = rs.getInt("id_utilizador_pagador");
                int idRecebedor = rs.getInt("id_utilizador_recebedor");

                // Adiciona o pagamento ao StringBuilder
                pagamentos.append(String.format("Pagamento ID: %d | Valor: %.2f | Data: %s | Pagador ID: %d | Recebedor ID: %d;\n",
                        idPagamento, valor, data, idPagador, idRecebedor));
            }
            pagamentos.append("FIM LISTAGEM");

            // Se nenhum pagamento for encontrado, retorna uma mensagem informando isso
            if (pagamentos.length() == 0) {
                return "Não há pagamentos associados ao grupo especificado.";
            }

            // Retorna a string com todos os pagamentos
            return pagamentos.toString().trim(); // Remove qualquer espaço extra no final

        } catch (SQLException e) {
            System.out.println("Erro ao listar pagamentos: " + e.getMessage());
            return "Erro ao listar pagamentos.";
        }
    }





    public static String eliminarPagamento(String nomeGrupo, int idPagamento, String dbFilePath) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath)) {
            // Verifica se o grupo existe
            String sqlVerificaGrupo = String.format(
                    "SELECT id_grupo FROM grupo WHERE nome = '%s'", nomeGrupo);

            int idGrupo;
            try (Statement stmtVerificaGrupo = conn.createStatement();
                 ResultSet rsGrupo = stmtVerificaGrupo.executeQuery(sqlVerificaGrupo)) {
                if (rsGrupo.next()) {
                    idGrupo = rsGrupo.getInt("id_grupo");
                } else {
                    return "Grupo não encontrado.";
                }
            }

            // Verifica se o pagamento existe e se os utilizadores associados pertencem ao grupo
            String sqlVerificaPagamento = String.format("""
                SELECT p.id_pagamento, p.id_utilizador_pagador, p.id_utilizador_recebedor 
                FROM pagamento p
                JOIN grupo_utilizador gu_pagador ON gu_pagador.id_utilizador = p.id_utilizador_pagador
                JOIN grupo_utilizador gu_recebedor ON gu_recebedor.id_utilizador = p.id_utilizador_recebedor
                WHERE p.id_pagamento = %d 
                AND gu_pagador.id_grupo = %d 
                AND gu_recebedor.id_grupo = %d
                """, idPagamento, idGrupo, idGrupo);

            try (Statement stmtVerificaPagamento = conn.createStatement();
                 ResultSet rsPagamento = stmtVerificaPagamento.executeQuery(sqlVerificaPagamento)) {
                if (!rsPagamento.next()) {
                    return "Pagamento não encontrado ou não está associado ao grupo.";
                }
            }

            // Elimina o pagamento da tabela pagamento
            String sqlDeletePagamento = String.format("""
                DELETE FROM pagamento 
                WHERE id_pagamento = %d
                """, idPagamento);

            try (Statement stmtDeletePagamento = conn.createStatement()) {
                int rowsAffected = stmtDeletePagamento.executeUpdate(sqlDeletePagamento);
                if (rowsAffected > 0) {
                    sendHeartbeat(conn,sqlDeletePagamento);
                    return "Pagamento eliminado com sucesso.";
                } else {
                    return "Erro ao eliminar pagamento. Nenhuma linha afetada.";
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao eliminar pagamento: " + e.getMessage());
            return "Erro ao eliminar pagamento.";
        }
    }

    public static String editarUser(String usernameAntigo, String novoUsername, String novaPassword, String dbFilePath) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath)) {
            // Verificar se o novo username já existe
            String sqlVerificaUsername = String.format(
                    "SELECT email FROM utilizador WHERE email = '%s'", novoUsername);

            try (Statement stmtVerifica = conn.createStatement();
                 ResultSet rsVerifica = stmtVerifica.executeQuery(sqlVerificaUsername)) {
                if (rsVerifica.next()) {
                    return "Erro: Já existe um utilizador com o novo nome de usuário.";
                }
            }

            // Atualizar o nome de usuário e a senha do utilizador
            String sqlAtualizarUtilizador = String.format("""
                UPDATE utilizador
                SET email = '%s', password = '%s'
                WHERE email = '%s'
                """, novoUsername, novaPassword, usernameAntigo);

            try (Statement stmtAtualizar = conn.createStatement()) {
                int rowsAffected = stmtAtualizar.executeUpdate(sqlAtualizarUtilizador);
                if (rowsAffected > 0) {
                    sendHeartbeat(conn,sqlAtualizarUtilizador);
                    return "Utilizador atualizado com sucesso.";
                } else {
                    return "Erro: Utilizador com o nome de usuário antigo não encontrado.";
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar utilizador: " + e.getMessage());
            return "Erro ao atualizar utilizador.";
        }
    }

    public static void criaNovaBaseDados(String dbPath){
        String url = "jdbc:sqlite:"+dbPath;

        String sql = """
                
                CREATE TABLE IF NOT EXISTS db_version(
                db_version REAL PRIMARY KEY 
);
                CREATE TABLE IF NOT EXISTS convite (
                    id_convite INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_grupo INTEGER,
                    id_utilizador_convidado INTEGER,
                    estado TEXT DEFAULT 'pendente',
                    FOREIGN KEY (id_grupo) REFERENCES grupo(id_grupo) ON DELETE CASCADE,
                    FOREIGN KEY (id_utilizador_convidado) REFERENCES utilizador(id_utilizador) ON DELETE CASCADE
                );

                CREATE TABLE IF NOT EXISTS grupo (
                    id_grupo INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL
                );

                CREATE TABLE IF NOT EXISTS utilizador (
                    id_utilizador INTEGER PRIMARY KEY AUTOINCREMENT,
                    email TEXT NOT NULL UNIQUE, 
                    password TEXT NOT NULL,
                    telefone NUMERIC NOT NULL UNIQUE
                                      
                );

                CREATE TABLE IF NOT EXISTS grupo_utilizador (
                    id_grupo INTEGER,
                    id_utilizador INTEGER,
                    PRIMARY KEY (id_grupo, id_utilizador),
                    FOREIGN KEY (id_grupo) REFERENCES grupo(id_grupo) ON DELETE CASCADE,
                    FOREIGN KEY (id_utilizador) REFERENCES utilizador(id_utilizador) ON DELETE CASCADE
                );
                CREATE TABLE IF NOT EXISTS despesa_utilizador (
                    id_despesa INTEGER,
                    id_utilizador INTEGER,
                    valor_participacao REAL NOT NULL,
                    PRIMARY KEY (id_despesa, id_utilizador),
                    FOREIGN KEY (id_despesa) REFERENCES despesa(id_despesa) ON DELETE CASCADE,
                    FOREIGN KEY (id_utilizador) REFERENCES utilizador(id_utilizador) ON DELETE CASCADE
                );

                CREATE TABLE IF NOT EXISTS despesa (
                    id_despesa INTEGER PRIMARY KEY AUTOINCREMENT,
                    descricao TEXT NOT NULL,
                    valor REAL NOT NULL,
                    data TEXT NOT NULL,  -- Formato: YYYY-MM-DD
                    id_grupo INTEGER,
                    id_utilizador INTEGER,
                    FOREIGN KEY (id_grupo) REFERENCES grupo (id_grupo),
                    FOREIGN KEY (id_utilizador) REFERENCES utilizador (id_utilizador)
                );

                CREATE TABLE IF NOT EXISTS pagamento (
                    id_pagamento INTEGER PRIMARY KEY AUTOINCREMENT,
                    valor REAL NOT NULL,
                    id_utilizador_pagador INTEGER,
                    id_utilizador_recebedor INTEGER,
                    id_despesa INTEGER,
                    data DATE NOT NULL,
                    FOREIGN KEY (id_utilizador_pagador) REFERENCES utilizador(id_utilizador) ON DELETE CASCADE,
                    FOREIGN KEY (id_utilizador_recebedor) REFERENCES utilizador(id_utilizador) ON DELETE CASCADE,
                    FOREIGN KEY (id_despesa) REFERENCES despesa(id_despesa) ON DELETE SET NULL
                );

                INSERT INTO db_version (db_version) VALUES (0.0);
                """;


        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Base de dados e tabelas criadas com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao criar a base de dados: " + e.getMessage());
        }
    }



}