//
// Created by riroc on 07/12/2023.
//

#ifndef ULTIMA_AULA3_IMOVEL_H
#define ULTIMA_AULA3_IMOVEL_H
#include "string"
#include "apartamento.h"
#include "loja.h"

using namespace std;

class Imovel {
public:
    int getArea() const;
    virtual int getPreco()const = 0;
    virtual string getCodigo() const = 0;
    int getAndar() const;
    Imovel(int _andar, int _area);
    virtual string getDescricao()const;

private:
    int andar, area, codNum;
    static int conta;
};


#endif //ULTIMA_AULA3_IMOVEL_H
