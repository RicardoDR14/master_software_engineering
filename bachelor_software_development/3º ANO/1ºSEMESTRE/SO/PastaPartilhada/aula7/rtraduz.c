// rtraduz.c
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

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

  if (fork() == 0) {
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
    int status;
    wait(&status);
  }
  return 0;
}