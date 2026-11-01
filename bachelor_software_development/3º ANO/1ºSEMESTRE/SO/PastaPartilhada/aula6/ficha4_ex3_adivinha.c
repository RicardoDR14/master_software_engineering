
#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <stdlib.h>

void handle_signal(int sig, siginfo_t *siginfo, void *ctx)
{
    int resposta = siginfo->si_value.sival_int;
    if (resposta == 0)
        printf("Acertou\n");
    else if (resposta == 1)
        printf("demasiado grande\n");
    else if (resposta == -1)
        printf("demasiado pequeno\n");
}

int main(int argc, char *argv[])
{
    if (argc != 2)
    {
        printf("Erro: Falta pid do sorteia\n");
        exit(1);
    }
    
    int pid_servidor = atoi(argv[1]);
    printf("O meu pid e: %d\n", getpid());

    struct sigaction sa;
    sa.sa_sigaction = handle_signal;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_RESTART | SA_SIGINFO;
    sigaction(SIGUSR2, &sa, NULL);

    int palpite;
    while (1)
    {
        printf("Insira o palpite: ");
        scanf("%d", &palpite);

        union sigval value;
        value.sival_int = palpite;
        sigqueue(pid_servidor, SIGUSR1, value);
    }
    return 0;
}