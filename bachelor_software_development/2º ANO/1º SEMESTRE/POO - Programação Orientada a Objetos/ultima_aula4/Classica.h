//
// Created by riroc on 14/12/2023.
//

#ifndef ULTIMA_AULA4_CLASSICA_H
#define ULTIMA_AULA4_CLASSICA_H

#include "CD.h"

class Classica : public CD {
public:
    string play() const override;
    Classica(string t, string m, string c);
    string descricao()const override;
    CD* duplica() const override;
private:
    string maestro;
    string compositor;
};


#endif //ULTIMA_AULA4_CLASSICA_H
