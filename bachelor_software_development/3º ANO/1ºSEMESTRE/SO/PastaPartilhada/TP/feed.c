#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>

#define PIPE_NAME "/manager_pipe"

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Erro sintaxe: ./feed <username>\n");
        return 1;
    }

    // Guarda o argumento numa variável
    char *username = argv[1];

    // Abrir o named pipe (FIFO) para escrita
    int fd = open(PIPE_NAME, O_WRONLY);
    if (fd == -1) {
        perror("Erro ao abrir o pipe");
        return 1;
    }

    // Escrever o username no pipe
    write(fd, username, strlen(username) + 1);

    printf("O username '%s' foi enviado para o manager.\n", username);

    close(fd);
    return 0;
}
