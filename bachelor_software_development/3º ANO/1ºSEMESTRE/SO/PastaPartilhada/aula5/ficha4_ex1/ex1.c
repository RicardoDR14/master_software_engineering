#include <stdio.h>
#include <signal.h>
#include <string.h>
#include <stdlib.h>

int contador = 0;

void handle_signal(int sig){
    if (contador <5)
    {
        printf("ai\n");
        contador++;
    }else{
        printf("ok, pronto!\n");
        exit(0);
    }
    
}

int main(){
    char nome[20];
    //signal(SIGINT, handle_signal);/// API antiga, nao usar
    struct sigaction sa;
    sa.sa_handler = handle_signal;
    sigemptyset(&sa.sa_mask);//LIMPAR A MASK
    sa.sa_flags = SA_RESTART;// flag que reinicia a execucao de funcoes bloqueantes, neste caso o scanf 
    sigaction(SIGUSR1,&sa,NULL);
    sigaction(SIGINT,&sa,NULL);
    while (1)
    {
        scanf("%s",&nome);
        if(strcmp(nome, "sair")==0){
            exit(0);
        }
        printf("Ola %s\n",nome);
    }
    return 0;
}