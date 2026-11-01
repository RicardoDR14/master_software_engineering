#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <ncurses.h>
//#include <ncurses.h>
#include "JOGOUI.H"

#define NLIN 16
#define NCOL 40

void mostrarJogo(char *filename, char map[NLIN][NCOL]){

	FILE *file = fopen(filename, "r");

	if(file == NULL){
		fprintf(stderr, "Erro ao abrir o arquivo %s\n", filename);
		return;
	}
}

int main(int argc, char* argv[]){

	if(argc != 2){
		fprintf(stderr, "USo: %s <Nome do Jogador>\n", argv[0]); //argv[0] = ./JOGOUI
		exit(EXIT_FAILURE);
	}

	initscr(); //Inicializar a biblioteca ncurse
	keypad(stdscr, TRUE);//Leitura de teclas especiais

	utilizador jogador;
	jogador.nome = argv[1];
	char *playerName = jogador.nome;

	//Verificar Comandos
	int ch;
	while((ch != getch() != KEY_SPACE){
		//O jogador continua a utilizar as teclas de direcao
		switch(ch){
			case KEY_LEFT:
				//MOVER PARA A ESQUERDA
				break;
			case KEY_RIGHT:
				//MOVER APARA A DIREITA
				break;
			case KEY_UP:
				//MOVER PARA CIMA
				break;
			case KEY_DOWN:
				//MOVER PARA BAIXO
				break
		}
	}

	while(1){
		comando = getch();

		if(comando == KEY_SPACE){
			//
			break;
		}

	int nivel = 0;//quando o jogo começar passa 1

	if(nivel == 1){
		mostrarJogo("labirinto_nivel1.txt");
	}else if(nivel == 2){
	 	mostrarJogo("labirinto_nivel2.txt");
	}else{
		mostrarJogo("labirinto_nivel3.txt");
	}

	return 0;
}
