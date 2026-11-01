#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>
#include <unistd.h>
#include <time.h>

int n = 20;
int contador = 0;
void funcSinal(int signum, siginfo_t *info, void *secret){
    
}


int pontuacao = 0;
int perdeu = 0;

void jogo()
{
    srand(time(NULL));
    int num1 = rand() % 100;
    int num2 = rand() % 100;
    int soma = num1 + num2, somaUser;
    n--;
    printf("\nIntroduza o valor da conta: [%d] + [%d]\n", num1, num2);
    if (scanf("%d", &somaUser) != 1){
        perror("")
    }
}


int main() {
    char nome[30];
    struct sigaction sa;
    sa.sa_sigaction = funcSinal;
    sa.sa_flags = SA_RESTART | SA_SIGINFO;
    sigaction(SIGALRM, &sa, NULL);
    while (1) {
        
        if (i == 0) {
            exit(1);
        }
        else{
            printf("Ola' %s\n", nome);}
    }
    return -1;
}
