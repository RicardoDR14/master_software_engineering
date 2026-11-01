//
// Created by riroc on 07/12/2023.
//

#include "apartamento.h"

string Apartamento::getDescricao() const {return Imovel::getDescricao() + "Assoalhadas: " + to_string(n_assoalhadas);}
Apartamento::Apartamento(int _area, int _andar, int a) : Imovel(_area, _andar), n_assoalhadas(a){}