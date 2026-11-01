

#ifndef ULTIMA_AULA_REGCIVIL_M_H
#define ULTIMA_AULA_REGCIVIL_M_H
#include <iostream>
#include "pessoa.h"
#include <vector>
#include <map>
using namespace std;
class RegCivil_m {
public:
    RegCiv_m(string _pais);
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
    map<int,Pessoa>::const_iterator procurapessoa(int bi) const;
    map<int,Pessoa>::iterator procurapessoa(int bi);
    map<int,Pessoa> pop;
    string pais;
};
};


#endif //ULTIMA_AULA_REGCIVIL_M_H
