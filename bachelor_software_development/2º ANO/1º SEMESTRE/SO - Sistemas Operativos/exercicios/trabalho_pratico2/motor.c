#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <string.h>
#include <time.h>
#include <sys/types.h>
#include <sys/wait.h>
#include "motor.h"

void recebeCoordenadas(int signo, siginfo_t *info, void *context) {
    struct Coordenadas *coordenadas = (struct Coordenadas *)info->si_value.sival_ptr;
    printf("RECEBI... %d %d %d\n", coordenadas->x, coordenadas->y, coordenadas->duracao);
}

int main(int argc, char *argv[]){

	if (argc != 1){ //executar o motor
		fprintf(stderr, "[ERRO] Nr.Args... <%s>\n", argv[0]);
	}

	
	FILE *file = fopen("labirinto_nivel3.txt", "r");
	if(file == NULL){
		perror("Erro ao abrir a file");
		return 1;
	}

	char linha[200];
	while(fgets(linha, sizeof(linha), file) !=NULL){
		printf("%s", linha);
	}

	fclose(file);
	

	char command[50];
	while(1){
		printf("Digite um comando(exit para sair): ");
        	fgets(command, sizeof(command), stdin);

        	// Remova a nova linha do final da string de comando
        	command[strcspn(command, "\n")] = '\0';

        	if (strcmp(command, "users") == 0) {
            		//listPlayers();
			printf("Sintaxe do Comando kick valido\n");
        	} else if (strstr(command, "kick") != NULL) {
            		char *name = strtok(command, " ");
            		name = strtok(NULL, " ");
			if (name != NULL) {
                		//kickPlayer(name);
				printf("Sintaxe do Comando kick valido\n");
            		}else {
                		printf("Sintaxe do Comando Invalido\n");
            		}
        	} else if (strcmp(command, "bots") == 0) { //lista os bots atualmente ativos
            		//listBots();
			printf("Sintaxe do Comando kick valido\n");
        	} else if (strcmp(command, "bmov") == 0) { //insere um bloqueio movel
			printf("Sintaxe do Comando kick valido\n");
		} else if (strcmp(command, "rbm") == 0)	{ //elimina o bloqueio movel
			printf("Sintaxe do Comando kick valido\n");
		} else if (strcmp(command, "begin") == 0) { //inicia manualmente o jogo
			printf("Sintaxe do Comando kick valido\n");
		} else if (strcmp(command, "end") == 0) { //encerra o jogo(se estiver a decorrer) e termina o motor
        	printf("Sintaxe do Comando kick valido\n");
		} else if (strcmp(command, "test_bot") == 0) {
			printf("Sintaxe do Comando kick valido\n");
			// Inicialização da estrutura sigaction para receber sinais
			struct sigaction sa;
			sa.sa_flags = SA_SIGINFO;
			sa.sa_sigaction = recebeCoordenadas;
			sigaction(SIGUSR1, &sa, NULL);
			// Cria um pipe para comunicação com o processo filho
			int pipe_fd[2];
			if (pipe(pipe_fd) == -1) {
				perror("Erro ao criar o pipe");
				exit(EXIT_FAILURE);
			}

			// Cria um processo filho
			pid_t pid = fork();

			if (pid == -1) {
				perror("Erro ao criar o processo filho");
				exit(EXIT_FAILURE);
			}

			if (pid == 0) {
				// Processo filho (bot)
				close(pipe_fd[0]); // Fecha a extremidade de leitura do pipe

				// Redireciona a saída padrão para o pipe
				dup2(pipe_fd[1], STDOUT_FILENO);
				close(pipe_fd[1]); // Fecha a extremidade de escrita do pipe

				// Executa o bot
				execlp("./bot", "./bot", "intervalo", "duracao", NULL);
				perror("Erro ao executar o bot");
				exit(EXIT_FAILURE);
			} else {
				// Processo pai (motor)
				close(pipe_fd[1]); // Fecha a extremidade de escrita do pipe

				// Espera pela resposta do processo filho
				waitpid(pid, NULL, 0);

				// Lê as coordenadas do pipe
				struct Coordenadas coordenadas;
				read(pipe_fd[0], &coordenadas, sizeof(struct Coordenadas));

				// Lida com as coordenadas
				printf("RECEBI... %d %d %d\n", coordenadas.x, coordenadas.y, coordenadas.duracao);

				close(pipe_fd[0]); // Fecha a extremidade de leitura do pipe
			}
        } else if (strcmp(command, "exit") == 0){
  			exit(EXIT_FAILURE);
		}else {
            	printf("Sintaxe do Comando Invalido\n");
        	}
	}
		//RECEBER O SINAL DO BOT -> EXEMPLIO: RECEBI: 3 5 10
	//LEITURA DO FICHEIRO COM MAPA -> SO DEVE LER E MOSTRAR NO ECRA
}





