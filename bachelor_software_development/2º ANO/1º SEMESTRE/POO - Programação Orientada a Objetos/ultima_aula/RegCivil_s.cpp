//
// Created by riroc on 16/11/2023.
//
#include <fstream>
#include <sstream>
#include "RegCivil_s.h"

using namespace std;

RegCivil_s::RegCivil_s(string _pais) : pais(_pais) {}

string RegCivil_s::getPais() const { return pais; }

bool RegCivil_s::novaPessoa(string nome, int bi, int nif) {
    vector<Pessoa>::iterator i = procurapessoa(bi);
    if (i == pop.end())
        return false;
    pop.emplace_back(nome,bi,nif);
    //pop.push_back(Pessoa(nome,bi,nif));
    return true;
}

int RegCivil_s::lerDeFicheiro(string ficheiro) {
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

bool RegCivil_s::apagaPessoa(int bi) {
    return pop.erase(Pessoa("whatever", bi,12345))==1;
}

string RegCivil_s::obtemNome(int bi) const {
    vector<Pessoa>::iterator i = procurapessoa(bi);
    if (i == pop.end())
        return "x"; //nao esta ca
    return i->getNome();
}

string RegCivil_s::listaPessoas() const {
    string res;
    for (const Pessoa &p: pop)
        //if (p.getBI() != -1)
        res += p.descricao() + "\n";
    return res;
}

bool RegCivil_s::atualizaNome(int bi, string nome) {
    vector<Pessoa>::iterator i = procurapessoa(bi);
    if (i == pop.end())
        return false;
    i->setNome(nome);
    return true;
}

int RegCivil_s::numPessoas() const {
    int res;
    for (const Pessoa &p: pop) {
        if (p.getBI() != -1)
            res++;
        return 1;
    }
    return -1;
}

bool RegCivil_s::gravaParaFicheiro(string ficheiro) const {
    ofstream of(ficheiro);
    if (!of.is_open()) { return false; }

    of << listaPessoas();
    of.close();
    return true;
}

int RegCivil_s::pessoasComNome(string s) const{
    int conta =0;
    for (const Pessoa &p :pop)
        if (p.getNome().find(s)!=string::npos)
            ++conta;
    return conta;
}

int RegCivil_s::apagaEstesBI(int de, int ate){
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

void RegCivil_s::apagaTodos(){
    pop.clear();
}

const Pessoa * RegCivil_s::getAwfulPointer2Pessoa(int bi) const {
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
vector<Pessoa>::iterator RegCivil_s::procurapessoa(int bi) const {

}
/*
int RegCiv::procurapessoa(int bi) const {
    for (const Pessoa &p : pop)
        if (p.getBI()==bi)
            return 0;
    return -1;
}*/
