#include "Regra.h"

using namespace std;

Regra::Regra(int _id, Sensor* sensor) : id(_id), sensor_associado(sensor) {}

igual_a::igual_a(int _id, Sensor* sensor, float _comparacao) : Regra(_id, sensor), valor_comparacao(_comparacao) {}

menor_que::menor_que(int _id, Sensor* sensor, float _comparacao) : Regra(_id, sensor), valor_comparacao(_comparacao) {}

maior_que::maior_que(int _id, Sensor* sensor, float _comparacao) : Regra(_id, sensor), valor_comparacao(_comparacao) {}

entre::entre(int _id, Sensor* sensor, float _min, float _max) : Regra(_id, sensor), valor_min(_min), valor_max(_max) {}

fora::fora(int _id, Sensor* sensor, float _min, float _max) : Regra(_id, sensor), valor_min(_min), valor_max(_max) {}

float Regra::getValorAtual() { return sensor_associado->getValorLeitura(); }

int Regra::getIdRegra() const { return id; }

int Regra::getIdSensorAss() const {return sensor_associado->getId();}

string Regra::getNomeSensorAss() const {return sensor_associado->getTipo();}

bool igual_a::avaliarRegra() {
    return valor_comparacao == getValorAtual();
}

bool menor_que::avaliarRegra() {
    return valor_comparacao < getValorAtual();
}

bool maior_que::avaliarRegra() {
    return valor_comparacao > getValorAtual();
}

bool entre::avaliarRegra() {
    return (valor_max > getValorAtual() && valor_min < getValorAtual());
}

bool fora::avaliarRegra() {
    return (valor_max < getValorAtual() && valor_min > getValorAtual());
}

string igual_a::getNomeRegra() { return " Regra Igual_a "; }

string menor_que::getNomeRegra() { return " Regra Menor_que "; }

string maior_que::getNomeRegra() { return " Regra Maior_que "; }

string entre::getNomeRegra() { return " Regra Entre "; }

string fora::getNomeRegra() { return " Regra Fora "; }