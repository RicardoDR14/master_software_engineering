//
// Created by riroc on 16/11/2023.
//

#include <iostream>
#include <string>
#include "pessoa.h"
#include <sstream>

using namespace std;

Pessoa::Pessoa(string _nome, int _bi, int _nif) : nome(_nome), bi(_bi), nif(_nif) { }
string Pessoa::getNome() const { return nome; }
int Pessoa::getBI() const { return bi; }
int Pessoa::getNIF() const { return nif; }
void Pessoa::setNome(string _nome) { nome = _nome; }
string Pessoa::descricao() const {
    ostringstream oss;
    oss << nome << bi << nif;
    return oss.str();
}
