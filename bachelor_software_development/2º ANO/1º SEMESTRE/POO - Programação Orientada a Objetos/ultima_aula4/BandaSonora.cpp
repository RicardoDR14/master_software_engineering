//
// Created by riroc on 14/12/2023.
//

#include "BandaSonora.h"

using namespace std;

BandaSonora::BandaSonora(std::string t, std::string f) : CD(t), filme(f) {}

string BandaSonora::play() const {
    return CD::play() + " filme: " + filme + " já não se fazem filmes como dantes";
}

string BandaSonora::descricao()const {
    return CD::descricao() + " Filme: " + filme;
}

CD *BandaSonora::duplica() const {return new BandaSonora(*this);}