#include "comandos.h"
#include "iteracao.h"

using namespace std;

bool validarInteiro(const string &str) {
    std::stringstream ss(str);
    int x;
    ss >> x;
    return !ss.fail() && ss.eof();
}

void executarComando(Iteracao *iteracao, const string &keyword, vector<string> &parametros) {

    if (keyword == "prox" && parametros.empty()) {
        iteracao->proximoInstante();
        iteracao->mostrarMensagens("Avancou um instante....\n", 0);
    } else if (keyword == "avanca" && parametros.size() == 1 && validarInteiro(parametros[0])) {
        int n = stoi(parametros[0]);
        iteracao->avancaInstantes(n);
        iteracao->mostrarMensagens("Avancou " + to_string(n) + " instantes....\n", 0);
    } else if (keyword == "hnova" && parametros.size() == 2 && validarInteiro(parametros[0]) &&
               validarInteiro(parametros[1])) {
        int n_linhas = stoi(parametros[0]);
        int n_colunas = stoi(parametros[1]);
        iteracao->criarHabitacao(n_linhas, n_colunas);
    } else if (keyword == "hrem" && parametros.empty()) {
        iteracao->removerHabitacao();
    } else if (keyword == "znova" && parametros.size() == 2 && validarInteiro(parametros[0]) &&
               validarInteiro(parametros[1])) {
        int linha = stoi(parametros[0]);
        int coluna = stoi(parametros[1]);
        iteracao->criarZona(linha, coluna);
    } else if (keyword == "zrem" && parametros.size() == 1 && validarInteiro(parametros[0])) {
        int idZona = stoi(parametros[0]);
        iteracao->removerZona(idZona);
    } else if (keyword == "zlista" && parametros.empty()) {
        iteracao->listarZonas();
    } else if (keyword == "zcomp" && parametros.size() == 1 && validarInteiro(parametros[0])) {
        int idZona = stoi(parametros[0]);
        iteracao->listarComponentes(idZona);
    } else if (keyword == "zprops" && parametros.size() == 1 && validarInteiro(parametros[0])) {
        int idZona = stoi(parametros[0]);
        iteracao->listarProps(idZona);
    } else if (keyword == "pmod" && parametros.size() == 3 && validarInteiro(parametros[0]) &&
               validarInteiro(parametros[2])) {
        int idZona = stoi(parametros[0]);
        string nome = parametros[1];
        float valor = stof(parametros[2]);
        iteracao->mudarValorProp(idZona, nome, valor);
    } else if (keyword == "cnovo" && parametros.size() == 3 && validarInteiro(parametros[0])) {
        int idZona = stoi(parametros[0]);
        string componente = parametros[1];
        string tipo_comando = parametros[2];
        iteracao->adicionaComponente(idZona, componente, tipo_comando);
    } else if (keyword == "crem" && parametros.size() == 3 && validarInteiro(parametros[0])) {
        int idZona = stoi(parametros[0]);
        string componente = parametros[1];
        int idComponente = stoi(parametros[2]);
        iteracao->removeComponente(idZona, componente, idComponente);
    } else if (keyword == "rnova") {
        int idZona = stoi(parametros[0]);
        int idProc = stoi(parametros[1]);
        string tipo_regra = parametros[2];
        int idSensor = stoi(parametros[3]);
        vector<float> valores;
        if (parametros.size() == 5) {
            valores.push_back(stof(parametros[4]));
        } else if (parametros.size() == 6) {
            valores.push_back(stof(parametros[4]));
            valores.push_back(stof(parametros[5]));
        }
        iteracao->criaRegra(idZona, idProc, tipo_regra, idSensor, valores);

    } else if (keyword == "pmuda" && parametros.size() == 3 && validarInteiro(parametros[0]) &&
               validarInteiro(parametros[1])) {
        int idZona = stoi(parametros[0]);
        int idProc = stoi(parametros[1]);
        string novo_comando = parametros[2];
        iteracao->mudaComandoProcessador(idZona,idProc,novo_comando);
    } else if (keyword == "rlista" && parametros.size() == 2 && validarInteiro(parametros[0]) &&
               validarInteiro(parametros[1])) {
        int idZona = stoi(parametros[0]);
        int idProc = stoi(parametros[1]);
        iteracao->listarRegras(idZona,idProc);
    } else if (keyword == "rrem" && parametros.size() == 3 && validarInteiro(parametros[0]) &&
               validarInteiro(parametros[1]) && validarInteiro(parametros[2])) {
        int idZona = stoi(parametros[0]);
        int idProc = stoi(parametros[1]);
        int idRegra = stoi(parametros[2]);
        iteracao->removeRegra(idZona,idProc,idRegra);
    } else if (keyword == "asoc" && parametros.size() == 2 && validarInteiro(parametros[0]) &&
               validarInteiro(parametros[1])) {
        int idZona = stoi(parametros[0]);
        int idProc = stoi(parametros[1]);
        int idApar = stoi(parametros[2]);
        iteracao->associaProcApar(idZona,idProc,idApar);
    } else if (keyword == "ades" && parametros.size() == 3 && validarInteiro(parametros[0]) &&
               validarInteiro(parametros[1]) && validarInteiro(parametros[2])) {
        int idZona = stoi(parametros[0]);
        int idProc = stoi(parametros[1]);
        int idApar = stoi(parametros[2]);
        iteracao->desassociaProcApar(idZona,idProc,idApar);
    } else if (keyword == "acom" && parametros.size() == 3 && validarInteiro(parametros[0]) &&
               validarInteiro(parametros[1])) {
        int idZona = stoi(parametros[0]);
        int idApar = stoi(parametros[1]);
        string novo_comando = parametros[2];
        iteracao->enviaComandoAparelho(idZona,idApar,novo_comando);
    } else if (keyword == "psalva" && parametros.size() == 3 && validarInteiro(parametros[0]) &&
               validarInteiro(parametros[1])) {
        iteracao->psalva();
    } else if (keyword == "prepoe" && parametros.size() == 1) {
        iteracao->prepoe();
    } else if (keyword == "prem" && parametros.size() == 1) {
        iteracao->prem();
    } else if (keyword == "plista" && parametros.empty()) {
        iteracao->plista();
    } else if (keyword == "exec" && parametros.size() == 1) {
        string nomeArquivo = parametros[0];
        iteracao->execCommand(nomeArquivo);
    } else if (keyword == "sair") {
        iteracao->mostrarMensagens("Sair......\n", 1);
        exit(0);
    } else
        cout << "Comando Inválido";// Caso chegue aqui Comando inválido
}
