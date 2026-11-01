#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <time.h>

int n1, n2;
int res = -1;
int n_segundos = 20;
int pontuação = 0;
int resposta;
int n_respostas_erradas = 0;

void handler_alarm(int sig)
{
    if (res != -1)
    {
        if (resposta == res)
            pontuação++;
        else
        {
            n_respostas_erradas++;
            if (n_respostas_erradas == 2)
            {
                printf("\nPerdeu o jogo, pontuação: %d\n", pontuação);
                exit(0);
            }
        }
    }
    n1 = rand() % 100;
    n2 = rand() % 100;
    printf("(%d sec) %d + %d = ", n_segundos, n1, n2);
    res = n1 + n2;
    resposta = -1;
    alarm(n_segundos--);
    if (n_segundos == 0)
    {
        printf("Ganhou o jogo com a pontuação %d!\n", pontuação);
        exit(0);
    }
}

int main()
{
    srand(time(NULL));
    setbuf(stdout, NULL);

    struct sigaction sa;
    sa.sa_handler = handler_alarm;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_RESTART;
    sigaction(SIGALRM, &sa, NULL);

    pid_t pid = getpid();
    union sigval value;
    sigqueue(pid, SIGALRM, value);

    while (1)
    {

        scanf("%d", &resposta);
        sigqueue(pid, SIGALRM, value);
    }

    return 0;
}