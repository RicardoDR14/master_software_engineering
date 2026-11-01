
#include "iteracao.h"
#include <sstream>
#include <fstream>
#include "comandos.h"

using namespace std;
using namespace term;

Iteracao::Iteracao(Terminal &t, Window &g, Window &m, Window &c)
        : terminal(t), JanelaGrafica(g), JanelaMensagens(m), JanelaComandos(c) {
}

void Iteracao::mostrarMensagens(const string &mensagem, int cor) {
    JanelaMensagens.clear();
    JanelaMensagens << set_color(0)<< "Mensagens>\n";
    JanelaMensagens << set_color(cor) << move_to(2,3)<< mensagem << "\n";
}

int Iteracao::getInstante() const {
    return instantes;
}

//comandos
void Iteracao::proximoInstante() {
    ++instantes;
    // Iterar sobre a matriz 'habitacao'
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                zona.alteracoesInstantes();
                mostrarMensagens("Avancou um instante",2);
            }
        }
    }
}


void Iteracao::avancaInstantes(int n) {
    int i=0;
    while (i!=n){
        proximoInstante();
        n++;
    }
    mostrarMensagens("Avancou "+ to_string(n)+" instantes",2);
}

void Iteracao::criarHabitacao(int numLinhas, int numColunas) {
    const int minLinhas = 2, maxLinhas = 4;
    const int minColunas = 2, maxColunas = 4;

    if (numLinhas < minLinhas || numLinhas > maxLinhas || numColunas < minColunas || numColunas > maxColunas) {
        mostrarMensagens("Dimensoes invalidas. \nLinhas e colunas devem ser entre 2 e 4.", 0);
        return;
    }

    habitacao.clear();
    habitacao.resize(numLinhas);
    for (auto &linha: habitacao) {
        linha.resize(numColunas);
    }

    zonaWindows.clear();

    mostrarMensagens("Habitacao criada com sucesso[" + to_string(numLinhas) + "x" + to_string(numColunas) + "]", 2);
}

void Iteracao::removerHabitacao() {
    if (habitacao.empty()) {
        mostrarMensagens("Nao existe nenhuma habitacao para remover.", 1);
        return;
    }

    habitacao.clear();

    zonaWindows.clear();

    mostrarMensagens("Habitacao removida com sucesso.", 2);
}

void Iteracao::criarZona(int linha, int coluna) {
    if (linha >= 0 && linha < habitacao.size() && coluna >= 0 && coluna < habitacao[linha].size()) {
        if (habitacao[linha][coluna]) {
            mostrarMensagens("Zona ja existe na posicao especificada.", 1);
            return;
        }

        int id = proxIdZona++;
        habitacao[linha][coluna] = make_unique<Zona>(id, linha, coluna);

        Window novaZona = terminal.create_window(5 + coluna * 15, 5 + linha * 4, 10, 4, true);
        novaZona << set_color(0) << id;

        zonaWindows[id] = make_shared<Window>(move(novaZona)); // Use std::make_shared para criar um shared_ptr

        mostrarMensagens("Zona criada na posicao [" + to_string(linha) + "," + to_string(coluna) + "].", 2);
    } else {
        mostrarMensagens("Indices fora dos limites ou habitacao nao existe.", 1);
    }
}

void Iteracao::removerZona(int idZona) {
    bool zonaRemovida = false;

    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna] && habitacao[linha][coluna]->getId() == idZona) {
                habitacao[linha][coluna].reset();
                zonaRemovida = true;
                break;
            }
        }
        if (zonaRemovida) break;
    }

    if (zonaRemovida) {
        zonaWindows.erase(idZona);

        mostrarMensagens("Zona removida com sucesso.", 2);
    } else {
        mostrarMensagens("Zona com ID " + to_string(idZona) + " nao encontrada.", 1);
    }
}

void Iteracao::listarZonas() {
    int zonaCount = 0;
    mostrarMensagens("Listagem das Zonas:",2);
    // Iterar sobre a matriz 'habitacao' e listar informacões de cada zona
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                const Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                string listagem = zona.zonaNumComp();
                mostrarMensagens(listagem,2);
                zonaCount++;
            }
        }
    }

    // Verificar se nenhuma zona foi encontrada
    if (zonaCount == 0) {
        mostrarMensagens("Nenhuma zona encontrada.", 1);
    }
}

void Iteracao::listarComponentes(int idZona) {
    int zonaExiste = 0;
    string listagem;
    // Iterar sobre a matriz 'habitacao'
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                if (zona.getId() == idZona) {
                    listagem = zona.listarComponentes(idZona);
                    mostrarMensagens(listagem, 2);
                    zonaExiste = 1;
                    break;
                }
            }
        }

        // Verificar se nenhuma zona foi encontrada
        if (zonaExiste == 0) {
            mostrarMensagens("Nenhuma zona encontrada.", 1);
        }
    }
}

void Iteracao::listarProps(int idZona) {
    int zonaExiste = 0;
    string listagem;
    // Iterar sobre a matriz 'habitacao'
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                if (zona.getId() == idZona) {
                    listagem = zona.listarProps(idZona);
                    mostrarMensagens(listagem, 2);
                    zonaExiste = 1;
                    break;
                }
            }
        }

        // Verificar se nenhuma zona foi encontrada
        if (zonaExiste == 0) {
            mostrarMensagens("Nenhuma zona encontrada.", 1);
        }
    }
}

void Iteracao::mudarValorProp(int idZona, string nome, float valor) {
    int zonaExiste = 0;
    // Iterar sobre a matriz 'habitacao'
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                if (zona.getId() == idZona) {//verifica se é tem o id pretendido
                    zona.setValueProp(nome, valor);
                    mostrarMensagens("Propriedade " + nome + "alterada\n", 2);
                    zonaExiste = 1;
                    break;
                }
            }
        }

        // Verificar se nenhuma zona foi encontrada
        if (zonaExiste == 0) {
            mostrarMensagens("Nenhuma zona encontrada.", 1);
        }
    }
}

void Iteracao::adicionaComponente(int idZona, string componente, string tipo_comando) {
    int zonaExiste = 0;
    // Iterar sobre a matriz 'habitacao'
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                if (zona.getId() == idZona) {//verifica se é tem o id pretendido
                    if (componente == "s") {
                        zona.addSensor(tipo_comando);
                    } else if (componente == "p") {
                        zona.addProcessador(tipo_comando);
                    } else if (componente == "a") {
                        zona.addAparelho(tipo_comando);
                    }
                    mostrarMensagens("Componente " + componente + " adicionado", 2);
                    zonaExiste = 1;
                    break;
                }
            }
        }

        // Verificar se nenhuma zona foi encontrada
        if (zonaExiste == 0) {
            mostrarMensagens("Nenhuma zona encontrada.", 1);
        }
    }
}

void Iteracao::removeComponente(int idZona, string componente, int idComponente) {
    int zonaExiste = 0;
    // Iterar sobre a matriz 'habitacao'
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                if (zona.getId() == idZona) {//verifica se é tem o id pretendido
                    if (componente == "s") {
                        zona.removeSensor(idComponente);
                    } else if (componente == "p") {
                        zona.removeProcessador(idComponente);
                    } else if (componente == "a") {
                        zona.removeAparelho(idComponente);
                    }
                    mostrarMensagens("Componente " + componente + " removido", 2);
                    zonaExiste = 1;
                    break;
                }
            }
        }

        // Verificar se nenhuma zona foi encontrada
        if (zonaExiste == 0) {
            mostrarMensagens("Nenhuma zona encontrada", 1);
        }
    }
}

void Iteracao::criaRegra(int idZona, int idProc, string tipo_regra, int idSensor, vector<float> valores) {
    int zonaExiste = 0;
    // Iterar sobre a matriz 'habitacao'
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                if (zona.getId() == idZona) {//verifica se é tem o id pretendido

                    ProcessadorRegras *p = zona.procurarProcessador(idProc);
                    Sensor *s = zona.procurarSensor(idSensor);
                    if (tipo_regra == "igual_a") {
                        p->adicionarRegraIgual_a(s, valores[0]);
                    } else if (tipo_regra == "menor_que") {
                        p->adicionarRegraMenor_que(s, valores[0]);
                    } else if (tipo_regra == "maior_que") {
                        p->adicionarRegraMaior_que(s, valores[0]);
                    } else if (tipo_regra == "entre") {
                        p->adicionarRegraEntre(s, valores[0], valores[1]);
                    } else if (tipo_regra == "fora") {
                        p->adicionarRegraFora(s, valores[0], valores[1]);
                    }
                    mostrarMensagens("Regra do tipo  " + tipo_regra + " adicionado", 2);
                    zonaExiste = 1;
                    break;
                }
            }
        }

        // Verificar se nenhuma zona foi encontrada
        if (zonaExiste == 0) {
            mostrarMensagens("Nenhuma zona encontrada", 1);
        }
    }
}


void Iteracao::mudaComandoProcessador(int idZona, int idProc, string novo_comando) {
    int zonaExiste = 0;
    // Iterar sobre a matriz 'habitacao'
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                if (zona.getId() == idZona) {//verifica se é tem o id pretendido

                    ProcessadorRegras *p = zona.procurarProcessador(idProc);
                    p->setComando(novo_comando);
                    mostrarMensagens("Comando alterado para '"+novo_comando+"'", 2);
                    zonaExiste = 1;
                    break;
                }
            }
        }

        // Verificar se nenhuma zona foi encontrada
        if (zonaExiste == 0) {
            mostrarMensagens("Nenhuma zona encontrada", 1);
        }
    }
}

void Iteracao::listarRegras(int idZona, int idProc) {
    int zonaExiste = 0;
    // Iterar sobre a matriz 'habitacao'
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                if (zona.getId() == idZona) {//verifica se é tem o id pretendido

                    ProcessadorRegras *p = zona.procurarProcessador(idProc);
                    string l = p->listarRegras();
                    mostrarMensagens(l,2);
                    zonaExiste = 1;
                    break;
                }
            }
        }

        // Verificar se nenhuma zona foi encontrada
        if (zonaExiste == 0) {
            mostrarMensagens("Nenhuma zona encontrada.", 1);
        }
    }

}

void Iteracao::removeRegra(int idZona, int idProc, int idRegra) {
    int zonaExiste = 0;
    // Iterar sobre a matriz 'habitacao'
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                if (zona.getId() == idZona) {//verifica se é tem o id pretendido

                    ProcessadorRegras *p = zona.procurarProcessador(idProc);
                    p->removerRegra(idRegra);
                    mostrarMensagens("Regra com id "+ to_string(idRegra)+" removido",2);
                    zonaExiste = 1;
                    break;
                }
            }
        }

        // Verificar se nenhuma zona foi encontrada
        if (zonaExiste == 0) {
            mostrarMensagens("Nenhuma zona encontrada.", 1);
        }
    }
}

void Iteracao::associaProcApar(int idZona, int idProc, int idApar) {
    int zonaExiste = 0;
    // Iterar sobre a matriz 'habitacao'
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                if (zona.getId() == idZona) {//verifica se é tem o id pretendido

                    ProcessadorRegras *p = zona.procurarProcessador(idProc);
                    Aparelho *a = zona.procurarAparelho(idApar);
                    p->adicionaAparelho(a);
                    mostrarMensagens("Associacao do Aparelho de id "+ to_string(idApar)+" estabelecida",2);
                    zonaExiste = 1;
                    break;
                }
            }
        }

        // Verificar se nenhuma zona foi encontrada
        if (zonaExiste == 0) {
            mostrarMensagens("Nenhuma zona encontrada.", 1);
        }
    }
}

void Iteracao::desassociaProcApar(int idZona, int idProc, int idApar) {
    int zonaExiste = 0;
    // Iterar sobre a matriz 'habitacao'
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                if (zona.getId() == idZona) {//verifica se é tem o id pretendido

                    ProcessadorRegras *p = zona.procurarProcessador(idProc);
                    p->removerAparelho(idApar);
                    mostrarMensagens("Associacao do Aparelho de id "+ to_string(idApar)+" removida",2);
                    zonaExiste = 1;
                    break;
                }
            }
        }

        // Verificar se nenhuma zona foi encontrada
        if (zonaExiste == 0) {
            mostrarMensagens("Nenhuma zona encontrada.", 1);
        }
    }
}

void Iteracao::enviaComandoAparelho(int idZona, int idApar, string novo_comando) {
    int zonaExiste = 0;
    // Iterar sobre a matriz 'habitacao'
    for (size_t linha = 0; linha < habitacao.size(); ++linha) {
        for (size_t coluna = 0; coluna < habitacao[linha].size(); ++coluna) {
            if (habitacao[linha][coluna]) {
                Zona &zona = *habitacao[linha][coluna]; // Obter referência para a zona

                if (zona.getId() == idZona) {//verifica se é tem o id pretendido

                    Aparelho *a = zona.procurarAparelho(idApar);
                    if (novo_comando == "ligar"){
                        if(!a->getEstado())
                            a->setEstado();
                    }
                    else if (novo_comando == "desligar"){
                        if(a->getEstado())
                            a->setEstado();
                    }
                    mostrarMensagens("Comando "+ novo_comando+" enviado",2);
                    zonaExiste = 1;
                    break;
                }
            }
        }

        // Verificar se nenhuma zona foi encontrada
        if (zonaExiste == 0) {
            mostrarMensagens("Nenhuma zona encontrada.", 1);
        }
    }
}

void Iteracao::psalva() {}

void Iteracao::prepoe() {}

void Iteracao::prem() {}

void Iteracao::plista() {}

void Iteracao::execCommand(const string &nomeArquivo) {
    ifstream arquivo(nomeArquivo);

    if (!arquivo.is_open()) {
        cerr << "Erro ao abrir o arquivo." << endl;
        return;
    }

    string linha;
    while (getline(arquivo, linha)) {
        istringstream iss(linha);
        string keyword;
        string p;
        vector<string> parametros;
        iss >> keyword;

        while (iss >> p) {//extrair todos os parametros do comando
            parametros.push_back(p);
        }
        executarComando(this, keyword, parametros);
    }
    arquivo.close();
}

