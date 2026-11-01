#ifndef JOGOUI_H
#define JOGOUI_H

#define max_nome 50

typedef struct{
	char nome[max_nome];
}utilizador;

void lerNome(utilizador *usuario);

#endif // JOGOUI_H
