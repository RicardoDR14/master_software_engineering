//
// Created by riroc on 19/10/2023.
//

#ifndef AULA5_AUTOMOVEL_H
#define AULA5_AUTOMOVEL_H
#include <iostream>
#include <string>

using namespace std;

class Automovel{
    const string matricula;
    int combustivel;
    const string marca;
    string cor;
public:
    Automovel(string mat, int comb, string mar, string cor);
    void config_automovel();

};

#endif //AULA5_AUTOMOVEL_H
