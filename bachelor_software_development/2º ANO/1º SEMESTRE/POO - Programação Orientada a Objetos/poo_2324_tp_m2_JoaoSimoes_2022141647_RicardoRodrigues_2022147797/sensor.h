#ifndef TRABALHOPRATICOCODIGO_SENSOR_H
#define TRABALHOPRATICOCODIGO_SENSOR_H

#include <iostream>
#include <vector>
#include <string>

using namespace std;

class Sensor{
public:
    Sensor(int id, string tipo, float* valor);
    ~Sensor();
    int getId() const;
    string getTipo() const;
    float getValorLeitura() const;

private:
    int id;
    string tipo;
    float* valor_leitura;
};


#endif //TRABALHOPRATICOCODIGO_SENSOR_H
