

#ifndef ULRIMA_AULA6_PONTO_H
#define ULRIMA_AULA6_PONTO_H
#include "iostream"

class Ponto {
public:
    Ponto(int cx, int cy);
    ~Ponto();
    void mostra() const;
private:
    int x,y;
};
#endif //ULRIMA_AULA6_PONTO_H
