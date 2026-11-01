#ifndef TRABALHOPRATICOCODIGO_PROCESSADOR_H
#define TRABALHOPRATICOCODIGO_PROCESSADOR_H
#include "Regra.h"
#include "aparelho.h"
#include <vector>
#include <iostream>
#include <sstream>
#include <algorithm>

using namespace std;

class ProcessadorRegras {
public:
    ProcessadorRegras(int _id, string _comando);
    int getId() const;
    int getNRegras() const;
    void setComando(string _comando);
    string listarRegras();
    void adicionaAparelho(Aparelho* a);
    void procLigaAparelho();
    void procDesligaAparelho();
    void removerAparelho(int id);
    void adicionarRegraIgual_a(Sensor* sensor, float valor_comparacao);
    void adicionarRegraMenor_que(Sensor* sensor, float valor_comparacao);
    void adicionarRegraMaior_que(Sensor* sensor, float valor_comparacao);
    void adicionarRegraEntre(Sensor* sensor, float minimo, float maximo);
    void adicionarRegraFora(Sensor* sensor, float minimo, float maximo);
    int removerRegra(int id);
    bool avaliarRegras();
private:
    int processador_id;
    int n_regras;
    string comando;
    vector<Regra*> regras;
    vector<Aparelho*> aparelhos;
};

#endif //TRABALHOPRATICOCODIGO_PROCESSADOR_H
