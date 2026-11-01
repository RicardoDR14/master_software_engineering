//
// Created by riroc on 14/12/2023.
//

#include "ColecaoCD.h"

void ColecaoCD::addCD(CD &p) {
    col.push_back(p.duplica());
}

ColecaoCD::~ColecaoCD() {
    for(CD * p: col)
        delete p;
}

ColecaoCD(const ColecaoCD &outro){
    for (CD * p : outro.col) {
        p.push_back(p->duplica());
    }
}