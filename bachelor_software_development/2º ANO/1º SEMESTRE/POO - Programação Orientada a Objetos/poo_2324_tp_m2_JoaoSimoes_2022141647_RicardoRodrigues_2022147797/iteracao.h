
#ifndef TRABALHOPRATICOCODIGO_ITERACAO_H
#define TRABALHOPRATICOCODIGO_ITERACAO_H
#include "Terminal.h"
#include <string>
#include <vector>
#include "Zona.h"
#include <map>
#include <memory>
using namespace std;
using namespace term;

class Iteracao {
public:
    Iteracao(Terminal &t, Window &g, Window &m, Window &c);

    void mostrarMensagens(const string& mensagem, int cor);
    int getInstante() const;
    //comandos
    void proximoInstante();
    void avancaInstantes(int n);
    void criarHabitacao(int numLinhas, int numColunas);
    void removerHabitacao();
    void criarZona(int linha, int coluna);
    void removerZona(int idZona);
    void listarZonas();
    void listarComponentes(int idZona);
    void listarProps(int idZona);
    void mudarValorProp(int idZona, string nome, float valor);
    void adicionaComponente(int idZona, string componente, string tipo_comando);
    void removeComponente(int idZona, string componente, int idComponente);
    void criaRegra(int idZona, int idProc, string tipo_regra, int idSensor, vector<float> valores);
    void mudaComandoProcessador(int idZona, int idProc, string novo_comando);
    void listarRegras(int idZona, int idProc);
    void removeRegra(int idZona, int idProc, int idRegra);
    void associaProcApar(int idZona, int idProc, int idApar);
    void desassociaProcApar(int idZona, int idProc, int idApar);
    void enviaComandoAparelho(int idZona, int idApar, string novo_comando);
    void psalva();
    void prepoe();
    void prem();
    void plista();
    void execCommand(const string& nomeArquivo);
private:
    Terminal &terminal;
    Window &JanelaGrafica;
    Window &JanelaMensagens;
    Window &JanelaComandos;
    int instantes = 0;
    int proxIdZona = 0;
    map<int, shared_ptr<Window>> zonaWindows;
    vector<vector<unique_ptr<Zona>>> habitacao;
};


#endif //TRABALHOPRATICOCODIGO_ITERACAO_H
