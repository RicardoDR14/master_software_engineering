//
// Created by riroc on 16/11/2023.
//

#ifndef ULTIMA_AULA_PESSOA_H
#define ULTIMA_AULA_PESSOA_H


#include <iostream>

using namespace std;

class Pessoa{
public:
    Pessoa();
    Pessoa(string nome, int bi, int nif);
    string getNome() const;
    int getBI() const;
    int getNIF() const;
    void setNome(string nome);
    string descricao() const;
private:
    string nome;
    int bi, nif;
};



#endif //ULTIMA_AULA_PESSOA_H
