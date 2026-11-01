#include <ostream>
#include "ponto.h"
#include <cmath>

using namespace std;

Ponto::Ponto(int x, int y):x(x),y(y){};

void Ponto::modifica_ponto(int px, int py){
    x=px;
    y=py;
}

double Ponto::dist_pontos(const Ponto& outro) const {
    int difX = x - outro.x;
    int difY = y - outro.y;
    return sqrt(difX * difX + difY * difY);
}
