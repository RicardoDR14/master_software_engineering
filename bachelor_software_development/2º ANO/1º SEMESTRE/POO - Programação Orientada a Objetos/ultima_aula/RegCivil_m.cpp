//
// Created by riroc on 16/11/2023.
//
#include <fstream>
#include <sstream>
#include "RegCivil_m.h"

using namespace std;

RegCivil_m::RegCiv_m(string _pais) : pais(_pais) {}

string RegCivil_m::getPais() const { return pais; }

bool RegCivil_m::novaPessoa(string nome, int bi, int nif) {
    pop[bi] = Pessoa(nome, bi, nif);
    return true;

}

int RegCivil_m::lerDeFicheiro(string ficheiro) {
    ifstream ifich(ficheiro);
    string linha, nome;
    int conta, bi, nif;
    conta = 0;
    while (ifich) {
        getline(ifich, linha);
        istringstream iss(linha);
        iss >> nome >> bi >> nif;
        if (novaPessoa(nome, bi, nif))
            conta++;
    }
    ifich.close();
    return conta;
}

bool RegCivil_m::apagaPessoa(int bi) {
    return pop.erase(bi)==1;
    //return pop.erase(Pessoa("whatever", bi,12345))==1; CASO DO SET
}

string RegCivil_m::obtemNome(int bi) const {
    auto it = pop.find(bi);
    if (it == pop.end())
        throw 404;
    return it->second.getNome();
}

string RegCivil_m::listaPessoas() const {
    string res;
    for (const pair<int,Pessoa> &p: pop)
        res += p.second.descricao() + "\n";
    return res;
}

bool RegCivil_m::atualizaNome(int bi, string nome) {
    auto i = pop.find(bi);
    if (i == pop.end())
        return false;
    i->second.setNome(nome);
    return true;
}

int RegCivil_m::numPessoas() const {
    return pop.size();
}

bool RegCivil_m::gravaParaFicheiro(string ficheiro) const {
    ofstream of(ficheiro);
    if (!of.is_open()) { return false; }

    of << listaPessoas();
    of.close();
    return true;
}

int RegCivil_m::pessoasComNome(string s) const {
    int conta = 0;
    for (const pair<int,Pessoa> &p: pop)
        if (p.second.getNome().find(s) != string::npos)
            ++conta;
    return conta;
}

int RegCivil_m::apagaEstesBI(int de, int ate) {
    int conta = 0;
    map<int,Pessoa>::const_iterator it = pop.cbegin();
    while (it != pop.end())
        if (it->second.getBI() >= de && it->second.getBI() <= ate) {
            it = pop.erase(it);
            ++conta;
        } else
            ++it;
    return conta;
}

void RegCivil_m::apagaTodos() {
    pop.clear();
}

const Pessoa *RegCivil_m::getAwfulPointer2Pessoa(int bi) const {
    for (const Pessoa &p: pop)
        if (p.getBI() == bi)
            return &p;
    return nullptr;
}


/*
int RegCiv::procurapessoa(int bi) const {
    for(int i=0; i<MAXPESSOAS; ++i)
        if(pop[i].getBI()==)
}
*/
map<int, Pessoa>::const_iterator RegCivil_m::procurapessoa(int bi) const{
   return pop.find(bi);
}

}
/*
int RegCiv::procurapessoa(int bi) const {
    for (const Pessoa &p : pop)
        if (p.getBI()==bi)
            return 0;
    return -1;
}*/
