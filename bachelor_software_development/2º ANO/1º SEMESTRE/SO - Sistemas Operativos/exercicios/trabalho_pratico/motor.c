#include "util.h"
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
			printf("Comando users valido\n");
        	} else if (strstr(command, "kick") != NULL) {
            		char *name = strtok(command, " ");
            		name = strtok(NULL, " ");
			if (name != NULL) {
                		//kickPlayer(name);
				printf("Comando kick valido\n");
            		}else {
                		printf("Sintaxe do Comando Invalido\n");
            		}
        	} else if (strcmp(command, "bots") == 0) { //lista os bots atualmente ativos
            		//listBots();
			printf("Comando bots valido\n");
        	} else if (strcmp(command, "bmov") == 0) { //insere um bloqueio movel
			printf("Comando bmov valido\n");
		} else if (strcmp(command, "rbm") == 0)	{ //elimina o bloqueio movel
			printf("Comando rbm valido\n");
		} else if (strcmp(command, "begin") == 0) { //inicia manualmente o jogo
			printf("Comando begin valido\n");
		} else if (strcmp(command, "end") == 0) { //encerra o jogo(se estiver a decorrer) e termina o motor
        	printf("Comando end valido\n");
		} else if (strcmp(command, "test_bot") == 0) {
			printf("Comando test_bot valido\n");
			// Inicialização da estrutura sigaction para receber sinais
		
			int fd[2];
			pipe(fd);

			int id = fork();
			char str[100];

			// Erro
    		if (id < 0) {
        		printf("Erro ao criar processo\n");
        		return -1;
    		}

    		// Filho
    		else if (id == 0) {
				close(1);
				dup(fd[1]);
				close(fd[1]);
				close(fd[0]);
				execl("bot","bot","1","2",NULL);
				printf("erro");
			
			}

            // Pai
			else{
				union sigval xpto;

				close(fd[1]);
				int s = read(fd[0],str,sizeof(str));
				printf ("%s",str);
				sigqueue(id,SIGINT,xpto);
				wait(&id);
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





