package pt.isec.pd.server.Database;

import org.springframework.stereotype.Service;
import pt.isec.pd.server.Data.Despesa;
import pt.isec.pd.server.Data.Grupo;
import pt.isec.pd.server.Data.Utilizador;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ManagerDB {

    private final String DATABASE_URL = "jdbc:sqlite:src\\main\\java\\pt\\isec\\pd\\server\\Database\\database.sqlite";
    private static Connection connection;

    public void initializeDatabase() {
        File dbFile = new File("src/main/java/pt/isec/pd/server/Database/database.sqlite");

        if (!dbFile.exists()) {
            System.out.println("A diretoria " + dbFile + " não existe!");
            criaNovaBaseDados(DATABASE_URL);
        }
        if (!dbFile.canRead()) {
            System.out.println("Sem permissões de leitura na diretoria " + dbFile + "!");
            return;
        }

        try {
            connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connected to the existing database.");
        } catch (SQLException e) {
            System.err.println("Failed to initialize the database: " + e.getMessage());
        }
    }

    public boolean autenticarUtilizador(String email, String password) {
        try {
            String query = "SELECT * FROM utilizador WHERE email = ? AND password = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean utilizadorExiste(String email) {
        try {
            String query = "SELECT * FROM utilizador WHERE email = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                return rs.next();  // Retorna true se o utilizador existir
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean registoUtilizador(String email, String password, String telefone) {
        try {
            try (Statement stmt = connection.createStatement()) {

                String sql = "INSERT INTO utilizador (email, password, telefone) VALUES ('" + email + "','" + password + "','" + telefone + "')";
                int rowsInserted = stmt.executeUpdate(sql);
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir utilizador na base de dados: " + e.getMessage());
            return false;
        }

    }

    public static List<Grupo> obterGrupos(String username) {
        List<Grupo> grupos = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {

            String sql = "SELECT g.id_grupo, g.nome FROM grupo g INNER JOIN grupo_utilizador gu ON g.id_grupo = gu.id_grupo INNER JOIN utilizador u ON gu.id_utilizador = u.id_utilizador WHERE u.email = '" + username + "'";
            stmt.execute(sql);

            // Executar a consulta
            ResultSet resultSet = stmt.getResultSet();

            // Verificar se o utilizador pertence a algum grupo
            while (resultSet.next()) {
                int groupId = resultSet.getInt("id_grupo");
                String groupName = resultSet.getString("nome");
                grupos.add(new Grupo(groupId, groupName));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grupos;
    }


    public static boolean grupoExiste(String groupName) {

        try (Statement stmt = connection.createStatement()) {

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

    public int obterIdGrupo(String groupName) {
        String query = "SELECT id_grupo FROM grupo WHERE nome = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, groupName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_grupo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Grupo obterGrupo(String groupName) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            String query = "SELECT * FROM Groups WHERE group_name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, groupName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Grupo(rs.getInt("id"), rs.getString("group_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean criarGrupo(String groupName, String creatorEmail) {

        try {
            connection.setAutoCommit(false);  // Usar transações

            try (Statement insertGroupStmt = connection.createStatement();
                 Statement getUserIdStmt = connection.createStatement();
                 Statement getGroupIdStmt = connection.createStatement();
                 Statement addMemberStmt = connection.createStatement()) {

                // Inserir o grupo
                String sqlInsertGroup = "INSERT INTO grupo(nome) VALUES('" + groupName + "')";
                insertGroupStmt.executeUpdate(sqlInsertGroup);


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
                connection.commit();  // Confirma a transação
                return true;
            } catch (SQLException e) {
                connection.rollback();  // Desfaz a transação em caso de erro
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static int inserirDespesa(String usernameInseriu, String nomeGrupo, String descricao, double valor, String participantes, String data) {

        try (Statement obterIdGrupoStmt = connection.createStatement();
             Statement inserirDespesaStmt = connection.createStatement();
             Statement inserirParticipacaoStmt = connection.createStatement();
             Statement obterIdUtilizadorStmt = connection.createStatement()) {

            // Obter ID do grupo
            String sqlObterIdGrupo = "SELECT gu.id_grupo FROM grupo_utilizador gu JOIN utilizador u ON gu.id_utilizador = u.id_utilizador JOIN grupo g ON gu.id_grupo = g.id_grupo WHERE u.email = '" + usernameInseriu + "' AND g.nome = '" + nomeGrupo + "'";
            ResultSet resultSetGrupo = obterIdGrupoStmt.executeQuery(sqlObterIdGrupo);
            if (!resultSetGrupo.next()) {
                System.out.println("Erro: O utilizador não pertence ao grupo especificado.");
                return -1;
            }
            int grupoId = resultSetGrupo.getInt("id_grupo");

            // Obter ID do utilizador que inseriu a despesa
            String sqlObterIdUtilizador = "SELECT id_utilizador FROM utilizador WHERE email = '" + usernameInseriu + "'";
            ResultSet resultSetUtilizador = obterIdUtilizadorStmt.executeQuery(sqlObterIdUtilizador);
            if (!resultSetUtilizador.next()) {
                System.out.println("Erro: Utilizador que inseriu a despesa não encontrado.");
                return -2;
            }
            int idInseriu = resultSetUtilizador.getInt("id_utilizador");

            // Inserir despesa na tabela `despesa`
            String sqlInserirDespesa = "INSERT INTO despesa (descricao, valor, id_grupo, data, id_utilizador) VALUES ('" + descricao + "', " + valor + ", " + grupoId + ", '" + data + "', " + idInseriu + ")";
            inserirDespesaStmt.executeUpdate(sqlInserirDespesa);

            ResultSet generatedKeys = inserirDespesaStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                System.out.println("Erro: Não foi possível obter o ID da despesa.");
                return -3;
            }
            int despesaId = generatedKeys.getInt(1);
            System.out.println("Despesa inserida com sucesso com ID: " + despesaId);

            List<String> participantesList = new ArrayList<>(Arrays.asList(participantes.split(",")));

            // Calcular o valor de participação para cada utilizador
            double valorParticipacao = valor / participantesList.size();

            // Inserir cada participante na tabela `despesa_utilizador`
            for (String participante : participantesList) {
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

            return despesaId;
        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
            return -5;
        }
    }

    public static List<Despesa> obterHistoricoDespesas(String nomeGrupo, String username) {
        List<Despesa> historico = new ArrayList<>();

        String sqlObterIdGrupo = """
                    SELECT gu.id_grupo
                    FROM grupo_utilizador gu
                    JOIN utilizador u ON gu.id_utilizador = u.id_utilizador
                    JOIN grupo g ON gu.id_grupo = g.id_grupo
                    WHERE u.email = ? AND g.nome = ?
                """;

        String sqlHistoricoDespesas = """
                    SELECT d.id_despesa, d.descricao, d.valor, d.data, u.email AS inserido_por
                    FROM despesa d
                    JOIN utilizador u ON d.id_utilizador = u.id_utilizador
                    WHERE d.id_grupo = ?
                    ORDER BY d.data ASC
                """;

        String sqlUtilizadoresEnvolvidos = """
                    SELECT u.email
                    FROM despesa_utilizador du
                    JOIN utilizador u ON du.id_utilizador = u.id_utilizador
                    WHERE du.id_despesa = ?
                """;

        try (PreparedStatement obterIdGrupoStmt = connection.prepareStatement(sqlObterIdGrupo);
             PreparedStatement historicoDespesasStmt = connection.prepareStatement(sqlHistoricoDespesas);
             PreparedStatement obterUtilizadoresStmt = connection.prepareStatement(sqlUtilizadoresEnvolvidos)) {

            // Obter ID do grupo
            obterIdGrupoStmt.setString(1, username);
            obterIdGrupoStmt.setString(2, nomeGrupo);
            ResultSet resultSetGrupo = obterIdGrupoStmt.executeQuery();

            if (!resultSetGrupo.next()) {
                return historico; // Retorna lista vazia se o utilizador não pertence ao grupo
            }

            int grupoId = resultSetGrupo.getInt("id_grupo");

            // Obter histórico de despesas
            historicoDespesasStmt.setInt(1, grupoId);
            ResultSet resultSetHistorico = historicoDespesasStmt.executeQuery();

            while (resultSetHistorico.next()) {
                int idDespesa = resultSetHistorico.getInt("id_despesa");
                String descricao = resultSetHistorico.getString("descricao");
                double valor = resultSetHistorico.getDouble("valor");
                String data = resultSetHistorico.getString("data");
                String inseridoPor = resultSetHistorico.getString("inserido_por");

                // Obter utilizadores envolvidos na despesa
                obterUtilizadoresStmt.setInt(1, idDespesa);
                ResultSet resultSetUtilizadores = obterUtilizadoresStmt.executeQuery();
                StringBuilder utilizadoresEnvolvidos = new StringBuilder();

                while (resultSetUtilizadores.next()) {
                    if (utilizadoresEnvolvidos.length() > 0) {
                        utilizadoresEnvolvidos.append(", ");
                    }
                    utilizadoresEnvolvidos.append(resultSetUtilizadores.getString("email"));
                }

                // Criar objeto despesa e adicionar à lista
                Despesa despesa = new Despesa(
                        grupoId, idDespesa, descricao, data, valor, inseridoPor, utilizadoresEnvolvidos.toString()
                );
                historico.add(despesa);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao obter histórico de despesas: " + e.getMessage());
            // Opcional: log mais detalhado em um arquivo ou sistema de monitoramento
        }

        return historico; // Retorna lista vazia em caso de erro ou grupo sem despesas
    }


    public static boolean eliminarGrupoPorNome(String nomeGrupo) {

        try (Statement verificaDespesasStmt = connection.createStatement();
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
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean eliminarDespesa(int idDespesa) {
        try {
            // Primeiro, vamos obter o ID da despesa com a descrição fornecida
            String sqlGetId = "SELECT id_despesa FROM despesa WHERE id_despesa = " + idDespesa;
            try (Statement pstmt = connection.createStatement()) {
                ResultSet rs = pstmt.executeQuery(sqlGetId);

                if (rs.next()) {

                    // Excluir da tabela despesa_utilizador
                    String sqlDeleteUtilizador = "DELETE FROM despesa_utilizador WHERE id_despesa = '" + idDespesa + "'";
                    try (Statement stmtDelete = connection.createStatement()) {
                        stmtDelete.executeUpdate(sqlDeleteUtilizador);
                    }

                    // Excluir da tabela despesa
                    String sqlDeleteDespesa = "DELETE FROM despesa WHERE id_despesa = " + idDespesa;
                    try (Statement stmtDeleteDespesa = connection.createStatement()) {
                        stmtDeleteDespesa.executeUpdate(sqlDeleteDespesa);
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public static void criaNovaBaseDados(String dbPathUrl) {

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


        try (Connection conn = DriverManager.getConnection(dbPathUrl); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Base de dados e tabelas criadas com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao criar a base de dados: " + e.getMessage());
        }
    }

    public boolean utilizadorNoGrupo(int groupId, int userId) {
        String query = "SELECT COUNT(*) FROM grupo_utilizador WHERE id_grupo = ? AND id_utilizador = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupId);
            statement.setInt(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int obterIdUtilizador(String userEmail) {
        String query = "SELECT id_utilizador FROM utilizador WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, userEmail);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_utilizador");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Grupo> obterTodosGrupos() {
        List<Grupo> grupos = new ArrayList<>();
        String query = "SELECT * FROM grupo";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                grupos.add(new Grupo(rs.getInt("id_grupo"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grupos;
    }

    public List<Utilizador> obterTodosUtilizadores() {
        List<Utilizador> utilizadores = new ArrayList<>();
        try {
            String query = "SELECT * FROM utilizador";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    Utilizador utilizador = new Utilizador(
                            rs.getInt("id_utilizador"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("telefone")
                    );
                    utilizadores.add(utilizador);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilizadores;
    }
}
