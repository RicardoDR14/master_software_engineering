//
// Created by riroc on 14/12/2023.
//

#ifndef ULTIMA_AULA4_CD_H
#define ULTIMA_AULA4_CD_H
#include <iostream>
#include <string>
#include <vector>
using namespace std;
class CD {
public:
    void addFaixa(string nome);
    CD(string t);
    virtual string descricao()const;
    virtual string play()const;
    virtual ~CD();
    virtual CD* duplica() const;
private:
    string titulo;
    vector<string> faixas;
};


#endif //ULTIMA_AULA4_CD_H
