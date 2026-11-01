//
// Created by riroc on 07/12/2023.
//

#ifndef ULTIMA_AULA3_APARTAMENTO_H
#define ULTIMA_AULA3_APARTAMENTO_H
#include "imovel.h"

class Apartamento:public Imovel {
public:
    string getDescricao() const override;
    Apartamento(int _area, int _andar, int a);

private:
    int andar;
    int n_assoalhadas;
};


#endif //ULTIMA_AULA3_APARTAMENTO_H
