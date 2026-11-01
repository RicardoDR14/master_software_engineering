#include <iostream>
#include <string>
#include "automovel.h"

using namespace std;

Automovel::Automovel(string mat, int comb, string mar, string cor) : matricula(mat), combustivel(comb), marca(mar), cor(cor){}

void Automovel::config_automovel(){
    cout << "\nMATRICULA: " << matricula << "\nCOMBUSTIVEL(%): " << combustivel << "\nMARCA: " << marca << "\n COR:" << cor << endl;
}
