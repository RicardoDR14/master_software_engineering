package pt.isec.deis.pd.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ManageDB {

    public static void main(String[] args) {

        String url = "jdbc:sqlite:src/resources/backup/database.sqlite";

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
                
                -- Inserir alguns grupos
                INSERT INTO grupo (nome) VALUES ('Amigos de Faculdade');
                INSERT INTO grupo (nome) VALUES ('Família');
                INSERT INTO grupo (nome) VALUES ('Colegas de Trabalho');
                
                -- Inserir alguns utilizadores
                INSERT INTO utilizador (email, password, telefone) VALUES ('joao@gmail.com', 'senha123', '912345678');
                INSERT INTO utilizador (email, password, telefone) VALUES ('maria@gmail.com', 'senha456', '923456789');
                INSERT INTO utilizador (email, password, telefone) VALUES ('ana@gmail.com', 'senha789', '934567890');
                
                -- Relacionar utilizadores com grupos
                INSERT INTO grupo_utilizador (id_grupo, id_utilizador) VALUES (1, 1); -- João no grupo "Amigos de Faculdade"
                INSERT INTO grupo_utilizador (id_grupo, id_utilizador) VALUES (1, 2); -- Maria no grupo "Amigos de Faculdade"
                INSERT INTO grupo_utilizador (id_grupo, id_utilizador) VALUES (2, 2); -- Maria no grupo "Família"
                INSERT INTO grupo_utilizador (id_grupo, id_utilizador) VALUES (2, 3); -- Ana no grupo "Família"
                
                -- Inserir convites
                INSERT INTO convite (id_grupo, id_utilizador_convidado, estado) VALUES (3, 1, 'pendente'); -- Convidar João para "Colegas de Trabalho"
                INSERT INTO convite (id_grupo, id_utilizador_convidado, estado) VALUES (3, 2, 'aceito');   -- Convidar Maria para "Colegas de Trabalho"
                
                -- Inserir despesas
                INSERT INTO despesa (descricao, valor, data, id_grupo, id_utilizador)\s
                VALUES ('Compra de pizza', 40.00, '2024-12-01', 1, 1); -- João registrou no grupo "Amigos de Faculdade"
                
                INSERT INTO despesa (descricao, valor, data, id_grupo, id_utilizador)\s
                VALUES ('Presente de Natal', 120.00, '2024-12-05', 2, 3); -- Ana registrou no grupo "Família"
                
                -- Relacionar utilizadores às despesas
                INSERT INTO despesa_utilizador (id_despesa, id_utilizador, valor_participacao) VALUES (1, 1, 20.00); -- João paga metade da pizza
                INSERT INTO despesa_utilizador (id_despesa, id_utilizador, valor_participacao) VALUES (1, 2, 20.00); -- Maria paga metade da pizza
                
                -- Pagamentos
                INSERT INTO pagamento (valor, id_utilizador_pagador, id_utilizador_recebedor, id_despesa, data)\s
                VALUES (20.00, 1, 2, 1, '2024-12-02'); -- João pagou a Maria pela pizza
                
                INSERT INTO pagamento (valor, id_utilizador_pagador, id_utilizador_recebedor, id_despesa, data)\s
                VALUES (30.00, 3, 2, NULL, '2024-12-06'); -- Ana pagou a Maria por outra despesa
                
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
