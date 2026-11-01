// rtraduz.c
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>
#include <string.h>

int main() {

  char op;
  char palavra[20];
  printf("Escolha a opcao: ");
  op = getchar();
  if (op == 'x') {
    return 0;
  }
  if (op != 'i' && op != 'f') {
    printf("opcao invalida\n");
    getchar(); // Ler o \n
    return 1;
  }
  printf("Insira a palavra: ");
  scanf("%s", palavra);
  getchar(); // Ler o \n

  int canal_re[2];
  if (pipe(canal_re)==-1){
    perror("Erro na criacao do pipe anonimo");
    exit(EXIT_FAILURE);
  }
  getchar();

  if (fork() == 0) {
//Manipular a tabela de filedescriptors
//close(0); //fecha stdin
//dup(canal_re[0]); // duplica canal na posição 0
dup2(canal_re[0],0);
close(canal_re[0]); 
close(canal_re[1]);

    if (op == 'i') {
      execl("./rding", "rding", NULL);
      perror("Erro a executar ding");
      exit(1);
    } else if (op == 'f') {
      execl("./rdfran", "rdfran", NULL);
      perror("Erro a executar dfran");
      exit(1);
    }
  } else {
    close(canal_re[0]);
    printf("Vou escrever no pipe\n");
    strcat(palavra,"\n"); // Acrescentar \n para destravar o scanf do filho
    int nbytes = write(canal_re[1], palavra, strlen(palavra));
    if (nbytes == -1){
      perror("Erro a escrever pipr anonimo");
    }
    printf("Escrevi %d bytes no pipe\n", nbytes);
    int status;
    wait(&status);

    close(canal_re[1]);
  }
  return 0;
}