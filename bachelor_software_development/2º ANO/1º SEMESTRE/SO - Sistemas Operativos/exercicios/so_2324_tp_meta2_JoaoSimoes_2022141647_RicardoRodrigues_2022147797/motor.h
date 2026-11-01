#ifndef motor_h
#define motor_h
#define TAMANHO_NOME 50


//guarda o nome e outras informações sobre o cliente;
typedef struct{
    char nome_jogador[TAMANHO_NOME];
    int id_jogador;
    int fd_servidor;
    int fd_servidor_resposta;
}Cliente;

struct cli_temp{
    char nome_jogador[TAMANHO_NOME];
    int id_jogador;
};

void test_bot();

#endif //motor_h
