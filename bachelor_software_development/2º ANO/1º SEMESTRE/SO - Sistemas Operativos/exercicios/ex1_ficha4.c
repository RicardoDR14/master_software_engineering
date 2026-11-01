#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>
#include <unistd.h>
int contador = 0;
void funcSinal(int signum, siginfo_t *info, void *secret){
    printf("\n Ai!!!! \n");
    if (++contador == 5){
        printf("\n ok, pronto \n");
        exit(1);
    }
}

int main() {
    char nome[30];
    struct sigaction sa;
    sa.sa_sigaction = funcSinal;
    //sa.sa_flags = SA_RESTART | SA_SIGINFO; // testarem sem esta flag
    sigaction(SIGINT, &sa, NULL);
    while (1) {
        printf("Digite o nome (ou 'sair' para terminar): ");
        fgets(nome, 30, stdin);
        nome[strlen(nome)-1] = '\0';
        if (strcmp(nome, "sair") == 0) {
            return 1;
        }
        else
        {printf("Ola' %s\n", nome);}
    }
    return -1;
}
