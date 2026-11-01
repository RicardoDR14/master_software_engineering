//
// Created by riroc on 19/10/2023.
//

#ifndef AULA5_2_PONTO_H
#define AULA5_2_PONTO_H

class Ponto{
    int x, y;
public:
    Ponto(int x, int y);
    void modifica_ponto(int px, int py);
    double dist_pontos(const Ponto& outro ) const;
};

#endif //AULA5_2_PONTO_H