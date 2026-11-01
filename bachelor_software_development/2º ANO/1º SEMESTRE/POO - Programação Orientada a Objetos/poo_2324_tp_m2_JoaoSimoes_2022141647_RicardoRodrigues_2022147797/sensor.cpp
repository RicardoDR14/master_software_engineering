#include "sensor.h"

using namespace std;

Sensor::Sensor(int id, string nome, float *valor) : id(id), tipo(nome), valor_leitura(valor) {}

Sensor::~Sensor() {}

int Sensor::getId() const { return id; }

string Sensor::getTipo() const {
    return tipo;
}

float Sensor::getValorLeitura() const { return *(valor_leitura); }