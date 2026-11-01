#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>

int main() {
    char letra;
    char palavra[100];
    pid_t pid;  // Variável para armazenar o PID do processo filho

    while (1) {
        // Pede a letra ao utilizador
        printf("Digite uma letra (x para sair, i para inglês, f para francês): ");
        scanf(" %c", &letra);

        // Se a letra for 'x', o programa termina
        if (letra == 'x') {
            printf("Programa terminado.\n");
            break;
        }

        // Pede a palavra ao utilizador
        printf("Digite uma palavra em português: ");
        scanf("%s", palavra);

        // Cria um processo filho usando fork()
        pid = fork();

        if (pid < 0) {
            // Erro ao criar o processo filho
            perror("Erro ao criar processo");
            exit(1);
        }
        else if (pid == 0) {
            // Estamos no processo filho
            if (letra == 'i') {
                // Executa o programa "ding" com a palavra como argumento
                execl("./ding", "ding", palavra, NULL);
                // Se execl falhar
                perror("Erro ao executar ding");
                exit(1);
            }
            else if (letra == 'f') {
                // Executa o programa "dfran" com a palavra como argumento
                execl("./dfran", "dfran", palavra, NULL);
                // Se execl falhar
                perror("Erro ao executar dfran");
                exit(1);
            }
            else {
                // Letra inválida no processo filho
                printf("Letra inválida!\n");
                exit(1);
            }
        }
        else {
            // Estamos no processo pai, aguardamos o filho terminar
            wait(NULL);
        }
    }

    return 0;
}
