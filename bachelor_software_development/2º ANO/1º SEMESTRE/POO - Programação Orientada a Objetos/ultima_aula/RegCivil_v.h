//
// Created by riroc on 16/11/2023.
//

#ifndef ULTIMA_AULA_REGCIVIL_V_H
#define ULTIMA_AULA_REGCIVIL_V_H

#include <iostream>
#include "pessoa.h"
#include <vector>

using namespace std;

class RegCiv{
public:
    RegCiv(string _pais);
    string getPais() const;
    bool novaPessoa(string nome, int bi, int nif) ;
    int lerDeFicheiro(string ficheiro);
    bool apagaPessoa(int bi);
    string obtemNome(int bi) const;
    string listaPessoas() const;
    bool atualizaNome(int bi, string nome);
    int numPessoas() const;
    bool gravaParaFicheiro(string ficheiro) const;

    int pessoasComNome(string s) const;
    int apagaEstesBI(int de, int ate);
    void apagaTodos();
    const Pessoa * getAwfulPointer2Pessoa(int bi) const;

private:
    vector<Pessoa>::const_iterator procurapessoa(int bi) const;
    //static const int MAXPESSOAS = 50;
    //Pessoa pop[MAXPESSOAS];
    vector <Pessoa> pop;
    string pais;
};

#endif //ULTIMA_AULA_REGCIVIL_V_H
