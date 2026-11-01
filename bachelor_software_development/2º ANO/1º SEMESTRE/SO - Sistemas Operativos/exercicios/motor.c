#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <signal.h>
#include <time.h>
#include <unistd.h>

struct DadosSinal {
    int x;
    int y;
    int duration;
};

void handler(int signum, siginfo_t* info, void* context) {
     struct DadosSinal *dados = (struct DadosSinal *)&info->si_value;
     printf("RECEBI: %d %d %d\n", dados->x, dados->y, dados->duration);
}

int main() {
    int pid_motor = getpid();
    printf("PID motor: %d\n", pid_motor);

    struct sigaction sa;
    sa.sa_sigaction = handler;
    sa.sa_flags = SA_SIGINFO;
    sigaction(SIGUSR1, &sa, NULL);

    while (1)
        ;

    return 0;
}