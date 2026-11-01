#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>

#define MAX_USERS 20
#define PIPE_NAME "/manager_pipe"

int main() {
    char usernames[MAX_USERS][100]; // Array para armazenar até 20 usernames
    int user_count = 0;             // Contador de utilizadores
    char buffer[100];               // Buffer para armazenar o username recebido
    int fd;

    // Criar o named pipe (FIFO)
    if (mkfifo(PIPE_NAME, 0666) == -1) {
        perror("Erro ao criar o named pipe");
        return 1;
    }

    printf("Manager iniciado. Aguardando conexões...\n");

    // Abrir o pipe para leitura
    fd = open(PIPE_NAME, O_RDONLY);
    if (fd == -1) {
        perror("Erro ao abrir o pipe");
        return 1;
    }

    while (1) {
        // Ler o username do pipe
        int bytes_read = read(fd, buffer, sizeof(buffer) - 1);
        if (bytes_read > 0) {
            buffer[bytes_read] = '\0'; // Certificar que a string é terminada corretamente

            // Verificar se o número máximo de utilizadores foi atingido
            if (user_count >= MAX_USERS) {
                printf("Erro: Número máximo de utilizadores atingido.\n");
                continue;
            }

            // Armazenar o username no array e incrementar o contador de utilizadores
            strcpy(usernames[user_count], buffer);
            user_count++;

            // Imprimir todos os utilizadores conectados
            printf("Utilizadores conectados (%d/%d):\n", user_count, MAX_USERS);
            for (int i = 0; i < user_count; i++) {
                printf(" - %s\n", usernames[i]);
            }
        }
    }

    close(fd);
    unlink(PIPE_NAME); // Remover o pipe ao terminar
    return 0;
}
