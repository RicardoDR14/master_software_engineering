//
// Created by riroc on 14/12/2023.
//

#ifndef ULTIMA_AULA4_COLECAOCD_H
#define ULTIMA_AULA4_COLECAOCD_H
#include "CD.h"

using namespace std;

class ColecaoCD {
public:
    void addCD(CD &p);
    ColecaoCD(const ColecaoCD& outro);
    ~ColecaoCD();


private:
    vector<CD*> col;
};


#endif //ULTIMA_AULA4_COLECAOCD_H
