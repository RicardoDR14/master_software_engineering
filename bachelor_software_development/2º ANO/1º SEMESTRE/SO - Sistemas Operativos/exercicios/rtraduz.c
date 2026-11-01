#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

int main() {
    char letra;
    char palavra[20];
    int pid, flag;
    int fd[2];
    int pd[2];
    pipe(fd);
    pipe(pd);

    // pipe[0] → read
    // pipe[1] → write

    printf("<TRADUZ> Insira uma letra: ");
    scanf(" %c", &letra);

    if (letra == 'x')
        return 0;

    printf("PID RTRADUZ: %d", getpid());

    pid = fork();

    // Erro
    if (pid < 0) {
        printf("Erro ao criar processo\n");
        return -1;
    }

    // Filho
    else if (pid == 0) {
        switch (letra) {
            case 'i':
                
                execl("./rding", "./rding", NULL);
                break;

            case 'f':
                close(0);      // Fecha stdin
                dup(fd[0]);    // fd[0] -> stdin
                close(fd[0]);  // Fecha fd[0]
                close(fd[1]);  // Fecha fd[1]

                close(1);      // Fecha stdout
                dup(pd[1]);    // pd[1] -> stdout
                close(pd[0]);  // Fecha pd[0]
                close(pd[1]);  // Fecha pd[1]

                execl("./rdfran", "./rdfran", NULL);
                break;

            default:
                break;
        }

    }

    // Pai
    else {
        if (letra == 'f') {
            printf("<TRADUZ> Insira uma palavra: ");
            scanf(" %s", palavra);

            close(fd[0]);                                   // Fecha fd[0]
            flag = write(fd[1], palavra, sizeof(palavra));  // Escreve no fd[1]
            if (flag == -1)
                printf("Erro ao escrever no pipe\n");
        }

        wait(&pid);

        if (letra == 'f') {
            close(pd[1]);
            flag = read(pd[0], palavra, sizeof(palavra));  // Lê do pd[0]
            if (flag == -1)
                printf("Erro ao ler do pipe\n");
            else
                printf("<TRADUZ/FRAN> %s\n", palavra);
        }
    }

    return 0;
}
