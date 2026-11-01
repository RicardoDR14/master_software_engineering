//
// Created by Rafael on 18/10/2023.
//

#ifndef FICHA31_TABLE_H
#define FICHA31_TABLE_H

const int TAM = 5;

class Tabela {
public:
    Tabela();
    void preenche(int val);
    void imprime() const;
    int obtemEaltera(int a, int b, int c);

private:
    int matriz[TAM][TAM];
};

#endif //FICHA31_TABLE_H
