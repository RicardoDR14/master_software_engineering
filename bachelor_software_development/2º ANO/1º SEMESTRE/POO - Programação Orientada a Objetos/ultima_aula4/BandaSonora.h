//
// Created by riroc on 14/12/2023.
//

#ifndef ULTIMA_AULA4_BANDASONORA_H
#define ULTIMA_AULA4_BANDASONORA_H

#include "CD.h"

class BandaSonora : public CD {
public:
    string play() const override;
    BandaSonora(string t, string f);
    string descricao() const override;
    CD* duplica() const override;
private:
    string filme;
};


#endif //ULTIMA_AULA4_BANDASONORA_H
