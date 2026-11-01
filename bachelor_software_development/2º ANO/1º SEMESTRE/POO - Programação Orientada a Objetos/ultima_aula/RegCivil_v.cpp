//
// Created by riroc on 16/11/2023.
//
#include <fstream>
#include <sstream>
#include "RegCivil_v.h"

using namespace std;

RegCiv::RegCiv(string _pais) : pais(_pais) {}

string RegCiv::getPais() const { return pais; }

bool RegCiv::novaPessoa(string nome, int bi, int nif) {
    vector<Pessoa>::const_iterator i = procurapessoa(bi);
    if (i == pop.end())
        return false;
    pop.emplace_back(nome,bi,nif);
    //pop.push_back(Pessoa(nome,bi,nif));
    return true;
}

int RegCiv::lerDeFicheiro(string ficheiro) {
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

bool RegCiv::apagaPessoa(int bi) {
    vector<Pessoa>::const_iterator i = procurapessoa(bi);
    if (i == pop.end())
        return false;
    pop.erase(i);
    return true;
}

string RegCiv::obtemNome(int bi) const {
    vector<Pessoa>::const_iterator i = procurapessoa(bi);
    if (i == pop.end())
        return "x"; //nao esta ca
    return i->getNome();
}

string RegCiv::listaPessoas() const {
    string res;
    for (const Pessoa &p: pop)
        //if (p.getBI() != -1)
            res += p.descricao() + "\n";
    return res;
}

bool RegCiv::atualizaNome(int bi, string nome) {
    vector<Pessoa>::const_iterator i = procurapessoa(bi);
    if (i == pop.end())
        return false;
    i->setNome(nome);
    return true;
}

int RegCiv::numPessoas() const {
    int res;
    for (const Pessoa &p: pop) {
        if (p.getBI() != -1)
            res++;
        return 1;
    }
    return -1;
}

bool RegCiv::gravaParaFicheiro(string ficheiro) const {
    ofstream of(ficheiro);
    if (!of.is_open()) { return false; }

    of << listaPessoas();
    of.close();
    return true;
}

int RegCiv::pessoasComNome(string s) const{
    int conta =0;
    for (const Pessoa &p :pop)
        if (p.getNome().find(s)!=string::npos)
            ++conta;
    return conta;
}

int RegCiv::apagaEstesBI(int de, int ate){
    int conta = 0;
    vector<Pessoa>::const_iterator it = pop.cbegin();
    while (it != pop.end())
        if (it->getBI() >= de && it->getBI() <= ate) {
            it = pop.erase(it);
            ++conta;
        } else
            ++it;
    return conta;
}

void RegCiv::apagaTodos(){
    pop.clear();
}

const Pessoa * RegCiv::getAwfulPointer2Pessoa(int bi) const {
    for(const Pessoa & p : pop)
        if(p.getBI() == bi)
            return & p;
    return nullptr;
}


/*
int RegCiv::procurapessoa(int bi) const {
    for(int i=0; i<MAXPESSOAS; ++i)
        if(pop[i].getBI()==)
}
*/
vector<Pessoa>::const_iterator RegCiv::procurapessoa(int bi) const {
    for (auto it = pop.begin(); it != pop.end(); ++it) {
        if (it->getBI() == bi) {
            return it;  // Return iterator pointing to the found Pessoa
        }
    }
    return pop.end();  // Return iterator pointing to the end if not found
}

/*
int RegCiv::procurapessoa(int bi) const {
    for (const Pessoa &p : pop)
        if (p.getBI()==bi)
            return 0;
    return -1;
}*/
