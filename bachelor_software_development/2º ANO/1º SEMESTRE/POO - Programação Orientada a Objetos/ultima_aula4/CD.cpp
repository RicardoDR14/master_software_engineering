//
// Created by riroc on 14/12/2023.
//

#include "CD.h"

using namespace std;

CD::CD(string t) : titulo(t) {}
CD::~CD() {}

void CD::addFaixa(string nome) {
    faixas.push_back(nome);
}

string CD::descricao() const {
    cout << "TítuloCD: " + titulo + " Faixas: ";
    for (const auto &faixa: faixas) {
        cout << faixa << " ";
    }
    cout << endl;
}

string CD::play()const {return titulo;}

CD *CD::duplica() const {return new CD(*this);}