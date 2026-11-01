#ifndef TRABALHOPRATICOCODIGO_REGRA_H
#define TRABALHOPRATICOCODIGO_REGRA_H

#include <iostream>
#include "sensor.h"
using namespace std;

class Regra {
public:
    Regra(int _id, Sensor* sensor);
    virtual bool avaliarRegra()=0;
    virtual string getNomeRegra()=0;
    float getValorAtual();
    int getIdRegra() const;
    int getIdSensorAss() const;
    string getNomeSensorAss() const;
private:
    int id;
    Sensor* sensor_associado; 
};

class igual_a:public Regra{
public:
    igual_a(int _id,Sensor* sensor, float _comparacao);
    bool avaliarRegra() override;
    string getNomeRegra() override;
private:
    float valor_comparacao;
};

class menor_que:public Regra{
public:
    menor_que(int _id,Sensor* sensor, float _comparacao);
    bool avaliarRegra() override;
    string getNomeRegra() override;
private:
    float valor_comparacao;
};

class maior_que:public Regra{
public:
    maior_que(int _id,Sensor* sensor, float _comparacao);
    bool avaliarRegra() override;
    string getNomeRegra() override;
private:
    float valor_comparacao;
};

class entre:public Regra{
public:
    entre(int _id,Sensor* sensor, float _min, float _max);
    bool avaliarRegra() override;
    string getNomeRegra() override;
private:
    float valor_min, valor_max;
};

class fora:public Regra{
public:
    fora(int _id,Sensor* sensor, float _min, float _max);
    bool avaliarRegra() override;
    string getNomeRegra() override;
private:
    float valor_min, valor_max;
};

#endif //TRABALHOPRATICOCODIGO_REGRA_H
