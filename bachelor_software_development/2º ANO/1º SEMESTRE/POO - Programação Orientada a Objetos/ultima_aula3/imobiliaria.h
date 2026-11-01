//
// Created by riroc on 07/12/2023.
//

#ifndef ULTIMA_AULA3_IMOBILIARIA_H
#define ULTIMA_AULA3_IMOBILIARIA_H
#include "imovel.h"
#include <vector>

class Imobiliaria {
    vector <Imovel *> imov;
public:
    void adiciona(Imovel &i);
    ~Imobiliaria();
};


#endif //ULTIMA_AULA3_IMOBILIARIA_H
