#ifndef JOGOUI_H
#define JOGOUI_H
#include <ncurses.h>

void desenhaMapa(WINDOW *janela, int tipo);
void trataTeclado(WINDOW *janelaTopo, WINDOW *janelaBaixo, WINDOW *janelaRight);

typedef struct{
    char nome_cliente[50];
    pid_t pid;
}Cliente;

#endif // JOGOUI_H
