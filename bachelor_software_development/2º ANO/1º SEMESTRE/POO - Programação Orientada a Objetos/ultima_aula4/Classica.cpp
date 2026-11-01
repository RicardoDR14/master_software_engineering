//
// Created by riroc on 14/12/2023.
//

#include "Classica.h"

using namespace std;

Classica::Classica(string t, string m, string c) : CD(t), maestro(m), compositor(c) {};

string Classica::play() const{
    return " a tocar música boa, Maestro: " + maestro + " Compositor: " + compositor;
}

string Classica::descricao()const {
    return CD::descricao() + " Maestro: " + maestro + " Compositor: " + compositor;
}

CD *Classica::duplica() const {return new Classica(*this);}