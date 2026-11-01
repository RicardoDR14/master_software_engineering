//
// Created by riroc on 07/12/2023.
//

#ifndef ULTIMA_AULA3_LOJA_H
#define ULTIMA_AULA3_LOJA_H

#include "imovel.h"
#include <string>

using namespace std;

class Loja:public Imovel{
public:
    int getPreco() const override;
    string getCodigo() const override;
    Loja(int _area);
    string getDescricao();

};


#endif //ULTIMA_AULA3_LOJA_H
