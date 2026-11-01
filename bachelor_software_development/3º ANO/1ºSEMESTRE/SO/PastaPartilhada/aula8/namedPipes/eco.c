#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/stat.h>

#define PIPE_NAME "pipe_eco"
#define BUFFER_SIZE 1024

int running = 1;

void handle_signal(int sig)
{
    running = 0;
}

int main()
{
    setbuf(stdout, NULL);

    // lidar com sinal SIGINT
    struct sigaction sa;
    sa.sa_handler = handle_signal;
    sa.sa_flags = 0;
    sigemptyset(&sa.sa_mask);
    sigaction(SIGINT, &sa, NULL);

    // criar name pipe
    if (access(PIPE_NAME, F_OK) != 0)
    {
        if (mkfifo(PIPE_NAME, 0666) == -1)
        {
            if (errno != EEXIST)
            { // verifficar o erro se for diferente do que já existe ou usar o access
                perror("Erro a criar named pipe");
                exit(EXIT_FAILURE);
            }
        }
    }

    // abrir named pipe para leitura
    int fd = open(PIPE_NAME, RD_ONLY);
    if (fd == -1)
    {
        perror("Erro na abertura do pipe");
        unlink(PIPE_NAME);
        exit(EXIT_FAILURE);
    }

    char buffer[BUFFER_SIZE];
    int nbytes;
    // ler do named pipe
    while (running)
    {
        nbytes = read(fd, buffer, BUFFER_SIZE);
        if (nbytes==-1){
            perror("Erro na leitura do named pipe")
            close(fd);
            unlink(PIPE_NAME);
            exit(EXIT_FAILURE);
        }

        printf("Leu %d bytes do named pipe\n",nbytes);

    }
    


    // close named pipe
    close(fd);
    // apagar named pipe
    unlink(PIPE_NAME);
}