#include "Ponto.h"

using namespace std;

// includes e outras declarações omitidos
Ponto::Ponto(int cx, int cy) : x(cx), y(cy) { cout << "CONSTR. "; mostra(); }
Ponto::~Ponto(){ cout << "DESTR. "; mostra(); }
void Ponto:: mostra() const { cout << "Ponto com " << x << "," << y << "\n"; }
