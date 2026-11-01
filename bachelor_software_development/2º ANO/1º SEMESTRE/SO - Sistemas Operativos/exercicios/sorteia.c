#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

int adivinhado = 0;
int num;
int flag = 0;

void exit(int s) {
    printf("\nAcertou %d numeros! Adeus...\n", adivinhado);
    flag = 1;
}

void sorteio() {
    srand(time(NULL));
    num = (rand() % 100) + 1;
}

void handler(int signum, siginfo_t* info, void* context) {
    int num_recebido = info->si_value.sival_int;
    int pid = info->si_pid;
    printf("\nRecebi o valor '%d' do PID[%d]!", num_recebido, pid);

    if (num_recebido > num)
        printf("\nMutio grande\n");
    else if (num_recebido < num)
        printf("\nMuito pequeno\n");
    else if (num_recebido == num) {
        printf("\nACERTOU!!!\nProximo numero...\n");
        ++adivinhado;
        sorteio();
    }
}

int main() {
    printf("PID: %d\n", getpid());

    struct sigaction sa;
    sa.sa_sigaction = handler;
    sa.sa_flags = SA_SIGINFO;
    sigaction(SIGUSR1, &sa, NULL);

    struct sigaction sa_int;
    sa_int.sa_handler = exit;
    sigaction(SIGINT, &sa_int, NULL);

    sorteio();
    while (flag != 1)
        ;

    return 0;
}