#include "util.h"
#include "JOGOUI.H"

#define NLIN 16
#define NCOL 40
#define TAMANHO_NOME 50

int c_l = 15;
int c_c = 20; // apenas para exemplo

void desenhaMapa(WINDOW *janela, int tipo)
{

    // quando temos o scroll ativo, não deveremos ter a borda desenhada na janela para não termos o problema escrever em cima das bordas
    if (tipo == 1)
    {
        // scrollok(janela, TRUE); // liga o scroll na "janela".
        // wprintw(janela, "\n #> ");
    }
    else
    {
        keypad(janela, TRUE);
        werase(janela);
        wclear(janela);
        wborder(janela, '|', '|', '-', '-', '+', '+', '+', '+');
    }
    refresh();
    wrefresh(janela);
}

void trataTeclado(WINDOW *janelaTopo, WINDOW *janelaBaixo)
{
    keypad(janelaTopo, TRUE);
    wmove(janelaTopo, 1, 1);

    int tecla = wgetch(janelaTopo);
    char comando[100];

    mvwprintw(janelaTopo, c_l, c_c, "A");
    wrefresh(janelaTopo);

    while (tecla != 113) // trata as tecla até introduzirem a letra q. O código asci de q é 113
    {

        if (tecla == KEY_UP)
        {
            desenhaMapa(janelaTopo, 2);
            mvwprintw(janelaTopo, c_l, c_c, " ");
            mvwprintw(janelaTopo, c_l - 1, c_c, "A");
            wrefresh(janelaTopo);
            c_l--;
        }
        else if (tecla == KEY_RIGHT)
        {
            desenhaMapa(janelaTopo, 2);
            mvwprintw(janelaTopo, c_l, c_c, " ");
            mvwprintw(janelaTopo, c_l, c_c + 1, "A");
            wrefresh(janelaTopo);
            c_c++;
        }
        else if (tecla == KEY_LEFT)
        {
            desenhaMapa(janelaTopo, 2);
            mvwprintw(janelaTopo, c_l, c_c, " ");
            mvwprintw(janelaTopo, c_l, c_c - 1, "A");
            wrefresh(janelaTopo);
            c_c--;
        }
        else if (tecla == KEY_DOWN)
        {
            desenhaMapa(janelaTopo, 2);
            mvwprintw(janelaTopo, c_l, c_c, " ");
            mvwprintw(janelaTopo, c_l + 1, c_c, "A");
            wrefresh(janelaTopo);
            c_l++;
        }
        else if (tecla == ' ')
        {
            echo();

            wprintw(janelaBaixo, "\n Comando(Digite 'exit' para sair)> ");

            wgetstr(janelaBaixo, comando);

            if (strcmp(comando, "players") == 0)
            {
                wprintw(janelaBaixo, "Sintaxe do Comando players valido\n");
                // listarJogadores(jogadores, numJogadores);
            }
            else if (strcmp(comando, "msg") == 0)
            {
                wprintw(janelaBaixo, "Sintaxe do Comando msg valido\n");
                // enviarMensagem(jogadores, numJogadores, arg1, arg2); -> aparece mensagem na JanelaRigth
                // mvwprintw(janelaRigth, 1, 1, "Chat");
            }
            else if (strcmp(comando, "exit") == 0)
            {
                wprintw(janelaBaixo, " Sintaxe do Comando exit valido\n");
                exit(1);
                // encerra o jogoUI(o motor deve ser avisado)
            }
            else
            {
                wprintw(janelaBaixo, "Sintaxe do comando Invalido\n");
            }

            wprintw(janelaBaixo, "\n [%s] ", comando);
            noecho();
            wrefresh(janelaBaixo);
        }
        wmove(janelaTopo, 1, 1);
        tecla = wgetch(janelaTopo);
    }
}

int main(int argc, char *argv[])
{

    if (argc != 2)
    {
        fprintf(stderr, "USo: %s <Nome do Jogador>\n", argv[0]); // argv[0] = ./JOGOUI
        exit(EXIT_FAILURE);
    }

    if (access("cliente_servidor", F_OK) != 0)
    {
        printf("[ERRO] O 'motor' nao esta a funcionar!\n");
        exit(1);
    }

    initscr();
    raw();
    noecho();
    keypad(stdscr, TRUE);
    attrset(A_DIM);

    mvprintw(1, 1, "Bem vindo");
    mvprintw(2, 10, "[ Up,Down,Right e Left comandos janela de cima ]");
    mvprintw(3, 10, "[ space - muda para o foco da janela de baixo / q - sair ]");

    WINDOW *janelaTopo = newwin(24, 82, 4, 1);
    WINDOW *janelaBaixo = newwin(15, 82, 30, 1);

    // ABRIR NAMED PIPES
    //--------------------------ENVIA INFORMACAO AO MOTOR--------------------------//
    Cliente cliente;
    // Abertura do File descriptor para escrita do nome do jogador
    int fd_cliente = open("cliente_servidor", O_WRONLY);

    char nome_jogador[TAMANHO_NOME];
    strncpy(nome_jogador, argv[1], sizeof(nome_jogador));
    strcpy(cliente.nome_cliente, nome_jogador);
    cliente.pid = getpid();

    char buffer[sizeof(cliente)];
    memcpy(buffer, &cliente, sizeof(cliente));
    // Tente abrir o FIFO para leitura várias vezes até ter sucesso

    if(write(fd_cliente, &buffer, sizeof(buffer)) == -1)
    {
        perror("Erro ao escrever no FIFO no JOGOUI");
        return 1; // Ou tome a ação apropriada
    }
    close(fd_cliente);

    // Abertura do File descriptor para a leitura de mensagens
    int fd_servidor = open("servidor_cliente", O_RDONLY);

    if (fd_servidor == -1)
    {
        perror("Erro ao abrir o FIFO servidor_cliente no JOGOUI");
        return 2;
    }

    // Configuração do timeout
    fd_set set;
    struct timeval timeout;
    FD_ZERO(&set);
    FD_SET(fd_servidor, &set);
    timeout.tv_sec = 5; // 5 segundos de timeout
    timeout.tv_usec = 0;

    // Espera por dados ou timeout
    int result = select(fd_servidor + 1, &set, NULL, NULL, &timeout);

    if (result == -1)
    {
        perror("Erro durante a chamada do select");
        close(fd_servidor);
        return 3;
    }
    else if (result == 0)
    {
        // Timeout ocorreu
        printf("Timeout - Não foram recebidos dados do servidor\n");
    }
    else
    {
        // Receber a matriz do labirinto do servidor
        char linhaRecebida[NCOL];

        // Lê cada linha do labirinto do FIFO no JOGOUI
        while (read(fd_servidor, linhaRecebida, sizeof(linhaRecebida)) > 0)
        {
            // Imprime a linha na janela
            // desenhaMapa(janelaTopo, 2);
            mvwprintw(janelaTopo, 1, 1, "%s",linhaRecebida);
            wrefresh(janelaTopo);

            //printf("%s", linhaRecebida);
        }
    }

    close(fd_servidor);

    desenhaMapa(janelaTopo, 2);
    wprintw(janelaBaixo, "Linha de Comandos\n");
    desenhaMapa(janelaBaixo, 1);
    trataTeclado(janelaTopo, janelaBaixo);
    wrefresh(janelaTopo);
    delwin(janelaTopo);
    wclear(janelaBaixo);
    wrefresh(janelaBaixo);
    delwin(janelaBaixo);
    endwin();

    return 0;
}
