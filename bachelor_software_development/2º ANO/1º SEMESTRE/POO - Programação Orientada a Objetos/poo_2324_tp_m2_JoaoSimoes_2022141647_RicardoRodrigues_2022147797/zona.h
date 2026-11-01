#ifndef TRABALHOPRATICOCODIGO_ZONA_H
#define TRABALHOPRATICOCODIGO_ZONA_H

#include "Processador.h"
#include "aparelho.h"
#include "sensor.h"
#include <string>
#include <map>
#include <iostream>
#include <sstream>
#include <algorithm>

using namespace std;

class Zona {

public:
    string listarComponentes(int id);
    string listarProps(int id);
    void setValueProp(string _tipo, float _valor);
    void addSensor(string nome);
    void addProcessador(string comando);
    void addAparelho(string tipo);
    void removeSensor(int _id);
    void removeProcessador(int _id);
    void removeAparelho(int _id);
    void alteracoesInstantes();
    Zona(int id, int l, int c);
    ~Zona();
    int getId() const;
    string getPosicao() const;
    int getNumSensores() const;
    int getNumProcessadores() const;
    int getNumAparelhos() const;
    string zonaNumComp() const;
    ProcessadorRegras *procurarProcessador(int id);
    Sensor *procurarSensor(int id);
    Aparelho *procurarAparelho(int id);
private:
    void init_props();
    int zona_id;
    int pos_lin, pos_col;
    int n_sensores, n_processadores, n_aparelhos;
    vector<Sensor*> sensores;
    vector<ProcessadorRegras *> processadores;
    vector<Aparelho *> aparelhos;
    map<string, float *> props;
};

#endif //TRABALHOPRATICOCODIGO_ZONA_H
