#include "zona.h"

using namespace std;

string Zona::listarComponentes(int id) {
    ostringstream oss;
    oss << "Descrição da zona de id: " + to_string(id) + "\n";
    for (auto s: sensores) {
        oss << "s " << s->getId()<< " " << s->getTipo() << " " << s->getValorLeitura() << endl;
    }
    oss << endl;
    for (auto p: processadores) {
        oss << "p " << p->getId() << " " << "processador" << " " << p->getNRegras();
    }
    oss << endl;
    for (auto a: aparelhos) {
        oss << "a " << a->getId() << " " << a->getNome() << " " << ((a->getEstado()) ? "ligado" : "desligado") << endl;
    }
    return oss.str();
}

string Zona::listarProps(int id) {
    ostringstream oss;
    oss << "Descrição das propriedades da zona de id " + to_string(id) + "\n";
    for (auto p: props)
        oss << p.first + ": " << p.second << "\n";
    return oss.str();
}

void Zona::setValueProp(string _tipo, float _valor) {
    props[_tipo] = new float(_valor);
}

void Zona::addSensor(string tipo) {
    ++n_sensores;
    sensores.push_back(new Sensor(n_sensores, tipo, props[tipo]));
}

void Zona::addProcessador(string comando) {
    ++n_processadores;
    processadores.push_back(new ProcessadorRegras(n_processadores, comando));
}

void Zona::addAparelho(string tipo) {
    ++n_aparelhos;
    if (tipo == "aquecedor") {
        aparelhos.push_back(new Aquecedor(n_aparelhos));
    } else if (tipo == "aspersor") {
        aparelhos.push_back(new Aspersor(n_aparelhos));
    } else if (tipo == "refrigerador") {
        aparelhos.push_back(new Refrigerador(n_aparelhos));
    } else if (tipo == "lampada") {
        aparelhos.push_back(new Lampada(n_aparelhos));
    }
}

void Zona::removeSensor(int _id) {
    --n_sensores;
    sensores.erase(remove_if(sensores.begin(), sensores.end(),
                             [_id](const Sensor *sensor) {
                                 return sensor->getId() == _id;
                             }),
                   sensores.end());
}

void Zona::removeProcessador(int _id) {
    --n_processadores;
    processadores.erase(remove_if(processadores.begin(), processadores.end(),
                                  [_id](const ProcessadorRegras *processador) {
                                      return processador->getId() == _id;
                                  }),
                        processadores.end());
}

void Zona::removeAparelho(int _id) {
    --n_aparelhos;
    aparelhos.erase(remove_if(aparelhos.begin(), aparelhos.end(),
                              [_id](const Aparelho *aparelho) {
                                  return aparelho->getId() == _id;
                              }),
                    aparelhos.end());
}

void Zona::alteracoesInstantes() {
    for (auto p:processadores) {
        if (p->avaliarRegras())
            p->procLigaAparelho();
        else
            p->procDesligaAparelho();
    }
    for (auto a:aparelhos) {

        if (a->getEstado()) {//está ligado
            a->incTemporizador();

            if (a->getNome()=="aquecedor"){
                if (a->getTemporizador()==1)
                    setValueProp("som", *(props["som"])+5);
                if (a->getTemporizador()%3==0 && *(props["temperatura"])<50){
                    setValueProp("temperatura",*(props["temperatura"])+1);
                }
            }

            if (a->getNome()=="aspersor"){
                if (a->getTemporizador()==1){
                    setValueProp("humidade", *(props["humidade"])+50);
                    setValueProp("vibracao",*(props["vibracao"])+100);
                }
                if (a->getTemporizador()==2){
                    setValueProp("fumo",0);
                }
            }

            if (a->getNome()=="refrigerador"){
                if (a->getTemporizador()==1)
                    setValueProp("som", *(props["som"])+20);
                if (a->getTemporizador()%3==0){
                    setValueProp("temperatura",*(props["temperatura"])-1);
                }
            }

            if (a->getNome()=="lampada"){
                if (a->getTemporizador()==1)
                    setValueProp("luz",*(props["luz"])+900);
            }

        } else{//está desligado

            if (a->getNome()=="aquecedor"){
                if (a->getTemporizador()==0)
                    setValueProp("som", *(props["luz"])-5);
            }

            if (a->getNome()=="aspersor")
                if (a->getTemporizador()==0)
                    setValueProp("vibracao", *(props["vibracao"])-50);


            if (a->getNome()=="refrigerador")
                if (a->getTemporizador()==0)
                    setValueProp("som", *(props["som"])-20);


            if (a->getNome()=="lampada")
                if (a->getTemporizador()==0)
                    setValueProp("luz",*(props["luz"])-900);

        }
    }
}

Zona::Zona(int id, int l, int c) : zona_id(id), pos_lin(l), pos_col(c), n_sensores(0), n_aparelhos(0),
                                   n_processadores(0) {
    init_props();
}

Zona::~Zona() {}

int Zona::getId() const { return zona_id; }

string Zona::getPosicao() const {
    ostringstream oss;
    oss <<  "posição: (" << pos_lin << "," << pos_col <<  ")\n";
    return oss.str();
}

int Zona::getNumSensores() const { return n_sensores; }

int Zona::getNumProcessadores() const { return n_processadores; }

int Zona::getNumAparelhos() const { return n_aparelhos; }

string Zona::zonaNumComp() const {
    ostringstream oss;
    oss << "ZONA -> Id: " << getId() << ", #Sensores: " << getNumSensores() << ", #Processadores: " << getNumProcessadores()
        << ", #Aparelhos: " << getNumAparelhos();
    return oss.str();
}

void Zona::init_props() {
    props["temperatura"] = new float(0.0);
    props["luz"] = new float(0.0);
    props["radiacao"] = new float(0.0);
    props["vibracao"] = new float(0.0);
    props["humidade"] = new float(0.0);
    props["fumo"] = new float(0.0);
    props["som"] = new float(0.0);
}

ProcessadorRegras *Zona::procurarProcessador(int id) {
    for (auto p:processadores) {
        if (p->getId()==id)
            return p;
    }
    return nullptr;
}

Sensor *Zona::procurarSensor(int id) {
    for (auto s:sensores) {
        if (s->getId()==id)
            return s;
    }
    return nullptr;
}

Aparelho *Zona::procurarAparelho(int id) {
    for (auto a:aparelhos) {
        if (a->getId()==id)
            return a;
    }
    return nullptr;
}