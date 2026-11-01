#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <string.h>
#include <time.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <pthread.h>
#include <errno.h>
#include <sys/socket.h>
#include <fcntl.h>
#include "motor.h"
#define MAX_CLIENTES 5
#define NLIN 16
#define NCOL 40
#define MAX_BOTS 10
#define MAX_STONES 50
#define MAX_LEVELS 3
#define MAX_MOVABLE_BLOCKS 5
#define MAP_ROWS 16
#define MAP_COLS 40
#define INSCRIPTION_PERIOD_ENV "INSCRICAO"
#define MIN_PLAYERS_ENV "NPLAYERS"
#define DURATION_ENV "DURACAO"
#define DECREMENT_ENV "DECREMENTO"

pthread_mutex_t ptrinco;

int id_jogador = 1000;
int numero_clientes = 0;

// Thread que lida com o Cliente
void *thread_cliente(void *arg, Cliente *arrayCliente)
{

	Cliente *cliente = (Cliente *)arg;

	pthread_mutex_lock(&ptrinco);
	// pthread_mutex_lock(&cliente -> mutex);

	// File descriptor que recebe mensagens do cliente
	int fd_servidor = open("cliente_servidor", O_RDONLY);
	cliente->fd_servidor = fd_servidor;

	// File descriptot que envia mensagens para o cliente
	int fd_servidor_resposta = open("servidor_cliente", O_WRONLY);
	cliente->fd_servidor_resposta = fd_servidor_resposta;

	char nome_jogador[TAMANHO_NOME];

	if (cliente->fd_servidor == -1)
	{
		perror("Erro ao abrir o FIFO cliente_servidor no motor\n");
		pthread_mutex_unlock(&ptrinco);
		return NULL;
	}

	// Receba os dados do named pipe
	char buffer[sizeof(struct cli_temp)];
	int bytes_read = read((cliente->fd_servidor), buffer, sizeof(buffer));

	// Converta o buffer de bytes para uma estrutura
	struct cli_temp cli_t;
	memcpy(&cli_t, buffer, sizeof(cli_t));

	// Guarde as variáveis na estrutura do motor cliente
	if (bytes_read == -1) // read(cliente->fd_servidor, &nome_jogador, sizeof(nome_jogador)) == -1)
	{
		perror("Erro ao ler FIFO cliente_servidor\n");
		close(cliente->fd_servidor);
		pthread_mutex_unlock(&ptrinco);
		return NULL;
	}

	if (cliente->fd_servidor_resposta == -1)
	{
		perror("Erro ao abrir o FIFO servidor_cliente no motor\n");
		close(cliente->fd_servidor_resposta);
		pthread_mutex_unlock(&ptrinco);
		return NULL;
	}

	// Verifica se o array esta cheio
	if (numero_clientes >= MAX_CLIENTES)
	{
		// Enviar mensagem para o cliente dizendo que o servidor está cheio
		char mensagem_limite_maximo[100] = "Limite máximo de jogadores atingido!!!Tenta mais tarde!!!";
		if (write(cliente->fd_servidor_resposta, &mensagem_limite_maximo, sizeof(mensagem_limite_maximo)) == -1)
		{
			perror("Erro ao escrever no FIFO servidor_cliente -> numero maximo de clientes atingido\n");
			close(cliente->fd_servidor_resposta);
			pthread_mutex_unlock(&ptrinco);
			return NULL;
		}
	}
	else
	{ // Verifica se ja existe o nome no array
		int nome_existe = 0;
		for (int i = 0; i < MAX_CLIENTES; i++)
		{
			if (strcmp(cliente->nome_jogador, cli_t.nome_jogador) == 0)
			{
				char mensagem_nome_existe[100] = "O nome ja existe!!!";
				//--------------------------ENVIA INFORMACAO AO JOGOUI--------------------------//
				if (write(cliente->fd_servidor_resposta, &mensagem_nome_existe, sizeof(mensagem_nome_existe)) == -1)
				{
					perror("Erro ao escrever no FIFO servidor_cliente -> o nome ja existe\n");
					close(cliente->fd_servidor_resposta);
					pthread_mutex_unlock(&ptrinco);
					return NULL;
				}
				nome_existe = 1;
				break;
			}
		}

		if (!nome_existe)
		{
			// Adicionar cliente ao array
			strcpy(cliente->nome_jogador, cli_t.nome_jogador);
			cliente->id_jogador = cli_t.id_jogador;
			numero_clientes++;
			id_jogador++;

			// Enviar mensagem de sucesso para o cliente
			/*char mensagem_sucesso[100] = "Bem Vindo!!!";
			if (write(cliente->fd_servidor_resposta, &mensagem_sucesso, sizeof(mensagem_sucesso)) == -1)
			{
				perror("Erro ao escrever no FIFO do servidor -> adicionar jogador\n");
				close(cliente->fd_servidor_resposta);
				pthread_mutex_unlock(&ptrinco);
				return NULL;
			}*/
			FILE *file1 = fopen("labirinto_nivel1.txt", "r");
			if (file1 == NULL) {
				perror("Erro ao abrir o arquivo do labirinto");
				return 1;
			} else {
				char linha[NCOL];
				while (fgets(linha, sizeof(linha), file1) != NULL) {
					if (write(cliente->fd_servidor_resposta, linha, sizeof(linha)) == -1) {
						perror("Erro ao escrever no FIFO do servidor -> enviar linha do labirinto");
						fclose(file1);
						close(cliente->fd_servidor_resposta);
						pthread_mutex_unlock(&ptrinco);
						return NULL;
					}
				}
				fclose(file1);
			}
		}
	}

	// pthread_mutex_unlock(&cliente -> mutex);
	close(cliente->fd_servidor_resposta);
	close(cliente->fd_servidor);
	pthread_mutex_unlock(&ptrinco);
	pthread_exit(NULL);
}

/*void *pthread_executa_comandos(void* arg){

}
*/

/*void *pthread_executa_bot(void* arg){

}
*/

/*void list_players(const Cliente *cliente)
{
	printf("Nome: %s\n", cliente -> nome_jogador);
	printf("Numero: %d\n", cliente -> numero_jogador);
}*/

void test_bot()
{
	char string[20];
	int canal[2], nbytes, res, estado;
	pipe(canal);
	res = fork();
	if (res == 0)
	{
		printf("[FILHO] Vou executar o bot...\n");
		close(canal[0]); // nao preciso
		close(1);
		dup(canal[1]);
		close(canal[1]);
		execl("bot", "bot", "3", "10", 5);
		fprintf(stderr, "[FILHO] ERRO - Nao consegui executar bot\n");
		exit(7);
	}
	close(canal[1]); // nao preciso
	do
	{
		nbytes = read(canal[0], string, sizeof(string) - 1);
		if (nbytes > 0)
		{
			string[nbytes] = '\0';
			printf("RECEBI: '%s' (%d bytes)\n", string, nbytes);
		}
	} while (nbytes > 0);
	close(canal[0]);
	wait(&estado);
	if (WIFEXITED(estado))
		printf("[EXECUTA] O bot:%d terminou (%d)\n", res, WIFEXITED(estado));
	return;
}

void kick_player(const char *nome, Cliente *arrayCliente) {
    int index = -1;

    // Encontrar o jogador pelo nome
    for (int i = 0; i < MAX_CLIENTES; i++) {
        if (strcmp(nome, arrayCliente[i].nome_jogador) == 0) {
            index = i;
            break;
        }
    }

    if (index != -1) {
        // Enviar sinal para JOGOUI terminar
        if (arrayCliente[index].id_jogador != -1) {
            kill(arrayCliente[index].id_jogador, SIGTERM);
			printf("Jogador foi expulso da partida\n");
			numero_clientes--;
        }

        // Limpar os dados do jogador
        strcpy(arrayCliente[index].nome_jogador, "-");
        arrayCliente[index].id_jogador = -1;
    }else{
		printf("O jogador com esse nome nao existe!!!");
	}
}


int main(int argc, char *argv[])
{

	if (argc != 1)
	{ // executar o motor
		fprintf(stderr, "[ERRO] Nr.Args... <%s>\n", argv[0]);
		exit(1);
	}

	pthread_t client[5], bot[3], comando;

	// Canal named piped para comunicar dos clientes para o servidor
	mkfifo("cliente_servidor", 0666);

	// Canal named piped para comunicar do servidor para os clientes
	mkfifo("servidor_cliente", 0666);

	// CRIA UM ARRAY DE TAMANHO MAX_CLIENTES
	Cliente arrayCliente[MAX_CLIENTES];
	// PREENCHE O ARRAY
	for (int i = 0; i < MAX_CLIENTES; i++)
	{
		strcpy(arrayCliente[i].nome_jogador, "-");
		arrayCliente[i].id_jogador = -1;
		arrayCliente[i].fd_servidor = -1;
		arrayCliente[i].fd_servidor_resposta = -1;
		// arrayCliente[i].ptrinco = PTHREAD_MUTEX_INITIALIZER;
	}

	pthread_mutex_init(&ptrinco, NULL);
	// Aguardar threads dos clientes
	for (int i = 0; i < MAX_CLIENTES; i++)
	{
		// Cria uma thread para cada cliente
		if (pthread_create(client + i, NULL, &thread_cliente, &arrayCliente[i]) != 0)
		{
			perror("Erro ao criar thread para o cliente");
			exit(EXIT_FAILURE);
		}
		// printf("Thread for criada\n");
	}
	printf("\n");

	printf("MOTOR(SERVIDOR): \n");
	char command[50];
	while (1)
	{

		printf("Digite um comando(Digite 'end' para sair): ");
		fgets(command, sizeof(command), stdin);

		// Remova a nova linha do final da string de comando
		command[strcspn(command, "\n")] = '\0';

		if (strcmp(command, "users") == 0)
		{
			for (int i = 0; i < MAX_CLIENTES; i++)
			{
				printf("\nJogador %d: \n", i + 1);
				// list_players(arrayCliente[i]);
				printf("Nome: %s\n", arrayCliente[i].nome_jogador);
				printf("Numero: %d\n", arrayCliente[i].id_jogador);
			}
			printf("\n");
		}
		else if (strstr(command, "kick") != NULL)
		{
			char *nome = strtok(command, " ");
			nome = strtok(NULL, " ");
			if (nome != NULL)
			{
				kick_player(nome, arrayCliente);
				printf("Sintaxe do Comando kick valido\n");
			}
			else
			{
				printf("Sintaxe do Comando Invalido\n <Kick> <nome do jogador>\n");
			}
		}
		else if (strcmp(command, "bots") == 0)
		{ // lista os bots atualmente ativos
			// listBots();
			printf("Sintaxe do Comando bots valido\n");
		}
		else if (strcmp(command, "bmov") == 0)
		{ // insere um bloqueio movel
			printf("Sintaxe do Comando bmov valido\n");
		}
		else if (strcmp(command, "rbm") == 0)
		{ // elimina o bloqueio movel
			printf("Sintaxe do Comando rbm valido\n");
		}
		else if (strcmp(command, "begin") == 0)
		{ // inicia manualmente o jogo
			printf("Sintaxe do Comando begin valido\n");
		}
		else if (strcmp(command, "test_bot") == 0)
		{
			printf("Sintaxe do Comando test_bot valido\n");
			test_bot();
		}
		else if (strcmp(command, "end") == 0)
		{
			break;
		}
		else
		{
			printf("Sintaxe do Comando Invalido\n");
		}
	}

	// Agora, podemos aguardar a conclusão das threads de clientes (se ainda estiverem em execução)
	for (int i = 0; i < MAX_CLIENTES; i++)
	{
		if (pthread_tryjoin_np(client[i], NULL) != 0)
		{
			// perror("Erro ao dar join a thread\n");
			exit(EXIT_FAILURE);
		}
		printf("Thread client %d terminou a execucao\n", i);
	}

	// Libere recursos e finalize o programa
	pthread_mutex_destroy(&ptrinco);
	unlink("cliente_servidor");
	unlink("servidor_cliente");
	exit(EXIT_SUCCESS);

	return 0;
}
