#include "util.h"
#include "JOGOUI.H"

#define NLIN 16
#define NCOL 40

// Função para adicionar um jogador ao array
/*void adicionarJogador(struct Jogador *jogadores, int *numJogadores, const char *nome) {
    if (*numJogadores < MAX_JOGADORES) {
        struct Jogador novoJogador;
        strncpy(novoJogador.nome, nome, sizeof(novoJogador.nome) - 1);
        novoJogador.nome[sizeof(novoJogador.nome) - 1] = '\0';
        jogadores[*numJogadores] = novoJogador;
        (*numJogadores)++;
    } else {
        fprintf(stderr, "Limite de jogadores atingido.\n");
    }
}*/


// Função para listar jogadores ativos
/*void listarJogadores(struct Jogador *jogadores, int numJogadores) {
    // Implemente a lógica para listar jogadores aqui
    for (int i = 0; i < numJogadores; ++i) {
        wprintw(janelaBaixo, "Jogador %d: %s\n", i + 1, jogadores[i].nome);
    }
    wrefresh(janelaBaixo);
}*/


// Função para enviar mensagem a um jogador específico
/*void enviarMensagem(struct Jogador *jogadores, int numJogadores, const char *destinatario, const char *mensagem) {
    // Implemente a lógica para enviar mensagem aqui
    // Procure o jogador pelo nome e envie a mensagem
    for (int i = 0; i < numJogadores; ++i) {
        if (strcmp(jogadores[i].nome, destinatario) == 0) {
            wprintw(janelaBaixo, "Mensagem enviada para %s: %s\n", destinatario, mensagem);
            wrefresh(janelaBaixo);
            return;
        }
    }
    wprintw(janelaBaixo, "Jogador %s não encontrado\n", destinatario);
    wrefresh(janelaBaixo);
}*/


// Função exemplo que faz o desenho de uma janela
// É chamada sempre que queremos atualizar o mapa
// Primeiro parâmetro  - recebe o ponteiro da janela (WINDOW)
// Segundo parâmetro   - recebe um inteiro, valor 1 - habilita o scrol na janel
void desenhaMapa(WINDOW *janela, int tipo)
{

    // quando temos o scroll ativo, não deveremos ter a borda desenhada na janela para não termos o problema escrever em cima das bordas
    if (tipo == 1)
    {
        scrollok(janela, TRUE); // liga o scroll na "janela".
        wprintw(janela, "\n #> ");
    }
    else 
    {
        keypad(janela, TRUE); // para ligar as teclas de direção (aplicar à janela)
        wclear(janela);// limpa a janela
        wborder(janela, '|', '|', '-', '-', '+', '+', '+', '+'); // Desenha uma borda. Nota importante: tudo o que escreverem, devem ter em conta a posição da borda
    }
    refresh(); // necessário para atualizar a janela
    wrefresh(janela); // necessário para atualizar a janela
}


// Função exemplo para tratar o teclado
// este exemplo aceita:
// as setas de direção para apresentar um texto na janelaTopo sem recurso à tecla enter
// tecla espaço para mudar o foco da janelaTopo para a janelaBaixo, e ativar a possibilidade de receber comandos (string) do utilizador
// letra q para finalizar o programa 
// O primeiro parâmetro da função é o ponteiro para a janela superior
// O segundo parâmetro da função é um ponteiro para a janela inferior
void trataTeclado(WINDOW *janelaTopo, WINDOW *janelaBaixo) 
{
    keypad(janelaTopo, TRUE);       // para ligar as teclas de direção (aplicar à janelaTopo)
    wmove(janelaTopo, 1, 1);        // posiciona o cursor,visualmente, na posicao 1,1 da janelaTopo
    //nota a posição é relativa à janelaTopo e não ao ecrã.
    int tecla = wgetch(janelaTopo); // MUITO importante: o input é feito sobre a janela em questão, neste caso na janelaTopo
    char comando[100];             // string que vai armazenar o comando

    while (tecla != 113) // trata as tecla até introduzirem a letra q. O código asci de q é 113
    {

        if (tecla == KEY_UP) // quando o utilizador introduz a seta para cima
        {
            desenhaMapa(janelaTopo, 2); // atualiza toda a janela
            mvwprintw(janelaTopo, 1, 1, "Estou a carregar na tecla UP na posição 1,1 "); // escreve a palavra UP na posição  1,1. 
            //Nota: não escreve na posição 0,0 porque está lá a borda da janela que foi criada anteriormente
            wrefresh(janelaTopo);// // necessário para atualizar a janelaTopo. Nota: é apenas esta janela que pretendemos atualizar
        }
        else if (tecla == KEY_RIGHT)
        {
            desenhaMapa(janelaTopo, 2);
            mvwprintw(janelaTopo, 1, 1, "Estou a carregar na tecla RIGHT na posição 1,1");
            wrefresh(janelaTopo);
        }
        else if (tecla == KEY_LEFT)
        {
            desenhaMapa(janelaTopo, 2);
            mvwprintw(janelaTopo, 1, 1, "Estou a carregar na tecla LEFT na posição 1,1");
            wrefresh(janelaTopo);
        }
        else if (tecla == KEY_DOWN)
        {
            desenhaMapa(janelaTopo, 2);
            mvwprintw(janelaTopo, 1, 1, "Estou a carregar na tecla DOWN na posição 1,1");
            wrefresh(janelaTopo);
        }
        else if (tecla == ' ') // trata a tecla espaço
        {  // a tecla espaço ativa a janela inferior e tem o scroll ativo
          //  o wprintw e o wgetstr devem ser utilizados em janelas que tem o scroll ativo.
            echo();                         // habilita a maneira como o utilizador visualiza o que escreve
                                             // ou seja volta a ligar o echo para se ver o que se está a escrever
            wprintw(janelaBaixo, "\n #> "); // utilizada para imprimir. 
                                            //nota como a janelaBaixo tem o scroll ativo, ele vai imprimindo linha a linha
            wgetstr(janelaBaixo, comando);  // para receber do teclado uma string na "janelaBaixo" para a variavel comando
			
			if(strcmp(comando, "players") == 0){
				wprintw(janelaBaixo, "Sintaxe do Comando players valido\n");
				//listarJogadores(jogadores, numJogadores);	
			} else if (strcmp(comando, "msg") == 0) {
				wprintw(janelaBaixo, "Sintaxe do Comando msg valido\n");
				//enviarMensagem(jogadores, numJogadores, arg1, arg2);
			} else if (strcmp(comando, "exit") == 0){
				wprintw(janelaBaixo," Sintaxe do Comando exit valido\n");
				//encerra o jogoUI(o motor deve ser avisado)
			} else {
				wprintw(janelaBaixo, "Sintaxe do comando Invalido\n");
			}

            wprintw(janelaBaixo, "\n [%s] ", comando);
            noecho(); //voltar a desabilitar o que o utilizador escreve
            wrefresh(janelaBaixo); //sempre que se escreve numa janela, tem de se fazer refresh
        }
        wmove(janelaTopo, 1, 1); // posiciona o cursor (visualmente) na posicao 1,1 da janelaTopo
        tecla = wgetch(janelaTopo); //espera que o utilizador introduza um inteiro. Importante e como já referido anteriormente introduzir a janela onde queremos receber o input
    }
}

int main(int argc, char* argv[]){

	// Inicializar array de jogadores
    struct Jogador *jogadores = (struct Jogador *)malloc(MAX_JOGADORES * sizeof(struct Jogador));
	if(argc != 2){
		fprintf(stderr, "USo: %s <Nome do Jogador>\n", argv[0]); //argv[0] = ./JOGOUI
		exit(EXIT_FAILURE);
	}

	// Inicializa assuntos de ncurses
    // -> mostra que tipo de inicialização pode ser necessária
    initscr(); // Obrigatorio e sempre a primeira operação de ncurses
    raw();  // desativa o buffer de input, cada tecla é lida imediatamente
    noecho();  // desliga o echo no ecrã, para voltar ativar devem utilizar a função echo();
    keypad(stdscr, TRUE);  // habilita as teclas  especiais, exemplo -> <-
    attrset(A_DIM);  //altera o brilho no print das janelas
    //as duas linhas seguintes são utilizadas para imprimir no ecrã, não utilizando nenhuma janela
    //as coordenas são relativas ao ecrã                                                               
    mvprintw(1, 10, "[ Up,Down,Right e Left comandos janela de cima ]");  // mensagem fora da janela, na linha 1, coluna 10 do ecrã
    mvprintw(2, 10, "[ space - muda para o foco da janela de baixo / q - sair ]"); // mensagem fora da janela, na linha 2, coluna 10 do ecrã
    WINDOW *janelaTopo = newwin(22, 82, 3, 1);  // Criar janela para a matriz de jogo, tendo os parametro numero de linhas,numero de colunas, posição onde começa a janela  e posição onde termina
    WINDOW *janelaBaixo = newwin(15, 82, 26, 1);  
    desenhaMapa(janelaTopo, 2);  // função exemplo que desenha o janela no ecrã
    desenhaMapa(janelaBaixo, 1);  // função exemplo que desenha o janela no ecrã
    trataTeclado(janelaTopo, janelaBaixo); // função exemplo que trata o teclado
    wclear(janelaTopo); // função que limpa o ecrã
    wrefresh(janelaTopo);  // função que faz atualização o ecrã com as operações realizadas anteriormente
    delwin(janelaTopo);  // apaga a janela.
    wclear(janelaBaixo); // função que limpa o ecrã
    wrefresh(janelaBaixo); // função que faz atualiza o ecrã com as operações realizadas anteriormente
    delwin(janelaBaixo);  // apaga a janela.
    endwin();  // encerra a utilização do ncurses. Muito importante senão o terminal fica inconsistente (idem se sair por outras vias)
	
	return 0;
}

