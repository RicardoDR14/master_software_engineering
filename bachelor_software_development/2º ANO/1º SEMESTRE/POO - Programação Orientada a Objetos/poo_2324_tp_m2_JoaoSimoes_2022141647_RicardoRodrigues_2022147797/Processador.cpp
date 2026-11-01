#include "Processador.h"

using namespace std;
ProcessadorRegras::ProcessadorRegras(int _id, string _comando) : processador_id(_id), comando(_comando), n_regras(0) {}

int ProcessadorRegras::getId() const {return processador_id;}

int ProcessadorRegras::getNRegras() const {return n_regras;}

void ProcessadorRegras::setComando(string _comando) {comando=_comando;}

string ProcessadorRegras::listarRegras() {
    ostringstream oss;
    oss<<"Regras do processador " << processador_id << endl;
    for (auto r : regras) {
        oss<< r->getNomeRegra()<< " " << r->getIdRegra() << " " << r->getNomeSensorAss()<<": "<<r->getIdSensorAss()<<endl;
    }
    return oss.str();
}

void ProcessadorRegras::adicionarRegraIgual_a(Sensor* sensor, float valor_comparacao) {
    ++n_regras;
    regras.push_back(new igual_a(n_regras,sensor,valor_comparacao));
}

void ProcessadorRegras::adicionarRegraMenor_que(Sensor* sensor, float valor_comparacao) {
    ++n_regras;
    regras.push_back(new menor_que(n_regras,sensor,valor_comparacao));
}

void ProcessadorRegras::adicionarRegraMaior_que(Sensor* sensor, float valor_comparacao) {
    ++n_regras;
    regras.push_back(new maior_que(n_regras,sensor,valor_comparacao));
}

void ProcessadorRegras::adicionarRegraEntre(Sensor* sensor, float minimo, float maximo) {
    ++n_regras;
    regras.push_back(new entre(n_regras,sensor,minimo,maximo));
}

void ProcessadorRegras::adicionarRegraFora(Sensor* sensor, float minimo, float maximo) {
    ++n_regras;
    regras.push_back(new fora(n_regras,sensor,minimo,maximo));
}

int ProcessadorRegras::removerRegra(int id) {
    --n_regras;
    regras.erase(remove_if(regras.begin(), regras.end(),
                           [id](const Regra *regra) {
                               return regra->getIdRegra() == id;
                           }),
                 regras.end());
}

void ProcessadorRegras::adicionaAparelho(Aparelho* a) {
    aparelhos.push_back(a);
}

void ProcessadorRegras::procLigaAparelho(){
    for (auto a:aparelhos) {
        if(!a->getEstado())
            a->setEstado();
    }
}

void ProcessadorRegras::procDesligaAparelho() {
    for (auto a:aparelhos) {
        if(a->getEstado())
            a->setEstado();
    }
}

void ProcessadorRegras::removerAparelho(int id) {
    aparelhos.erase(remove_if(aparelhos.begin(), aparelhos.end(),
                              [id](const Aparelho *aparelho) {
                                  return aparelho->getId() == id;
                              }),
                    aparelhos.end());
}

bool ProcessadorRegras::avaliarRegras() {
    for (auto r:regras) {
        if (!r->avaliarRegra())
            return false;
    }
    return true;
}