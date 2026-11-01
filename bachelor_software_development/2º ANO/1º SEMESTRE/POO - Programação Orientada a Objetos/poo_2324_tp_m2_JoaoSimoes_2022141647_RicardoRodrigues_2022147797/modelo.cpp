
#include "modelo.h"
#include "comandos.h"
#include <sstream>
#include <iomanip>

Modelo::Modelo()
        : terminal(Terminal::instance()),
          JanelaGrafica(terminal.create_window(4, 2, 60, 20, true)),
          JanelaMensagens(terminal.create_window(64, 2, 50, 20, true)),
          JanelaComandos(terminal.create_window(4, 22, 110, 3, true)),
          iteracao(terminal,JanelaGrafica,JanelaMensagens,JanelaComandos)
{}

void Modelo::run() {
    for (int i = 1; i < 20; i++) {
        terminal.init_color(i, i, 0);
    }

    Window JanelaGrafica = terminal.create_window(4, 2, 60, 20, true);
    Window JanelaMensagens = terminal.create_window(64, 2, 50, 20, true);
    Window JanelaComandos = terminal.create_window(4, 22, 110, 4, true);

    JanelaGrafica << set_color(0) << "JanelaGrafica>\n";
    JanelaMensagens << set_color(0)<< "Mensagens>\n";

    string comando;

    while (true) {
        JanelaComandos.clear();
        JanelaComandos << set_color(0) << move_to(1, 0) << "Instante: " << iteracao.getInstante() << "\n";
        JanelaComandos << set_color(0) << move_to(1, 1) << "Comando>";
        JanelaComandos >> comando;

        istringstream iss(comando);
        string keyword;
        string p;
        vector<string> parametros;
        iss >> keyword;

        while (iss >> p) {//extrair todos os parametros do comando
            parametros.push_back(p);
        }
        executarComando(&iteracao , keyword, parametros);
    }
}
