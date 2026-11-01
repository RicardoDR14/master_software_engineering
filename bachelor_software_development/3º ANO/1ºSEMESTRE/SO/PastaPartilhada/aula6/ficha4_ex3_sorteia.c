//Sorteia.c
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <time.h>
#include <fcntl.h>

int numero_sorteado;
int contador = 0;

#define TMP_FILE "sorteia.lock"

void handle_signal(int sig, siginfo_t *siginfo, void *ctx){
    if (sig == SIGINT){
        printf("O numero sorteado foi adivinhado %d vezes\n", contador);
        if(unlink(TMP_FILE) == -1){
            perror("erro a apagar o ficheiro");
        }
        exit(0);
    }
    pid_t pid = siginfo->si_pid; // Obter o pid do processo que enviou o sinal
    int palpite = siginfo->si_value.sival_int; //Obter o valor que foi enviado com o sinal
    printf("Recebi o palpite %d do processo %d\n", palpite, pid);
    int resposta_adivinha;
    if(palpite == numero_sorteado){
        resposta_adivinha = 0;
        contador++;
        numero_sorteado = rand() % 100 + 1;
    } else if (palpite > numero_sorteado){
        resposta_adivinha = 1;
    } else if (palpite < numero_sorteado){
        resposta_adivinha = -1;
    }
    union sigval value;
    value.sival_int = resposta_adivinha;
    sigqueue(pid, SIGUSR2, value);
}

int main(){
    srand(time(NULL));
    setbuf(stdout, NULL);

    if(access(TMP_FILE, F_OK) == 0){
        printf("Ja existe um sorteia em execucao\n");
        exit(0);
    }

    int fd;
    if((fd= open(TMP_FILE, O_CREAT, 0600 /*permissoes*/)) == -1){
        perror("Create file");
        exit(EXIT_FAILURE);
    }
    close(fd);

    if(fork() != 0){
        exit(0);
    }
    printf("O meu pid e: %d\n", getpid());

    numero_sorteado = rand() % 100 + 1;
    
    struct sigaction sa;
    sa.sa_sigaction = handle_signal;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_RESTART | SA_SIGINFO;
    sigaction(SIGUSR1, &sa, NULL);
    sigaction(SIGINT, &sa, NULL);

    while(1){
        pause();
    }
    return 0;
}