//
// Created by riroc on 07/12/2023.
//
#include "imovel.h"

Imovel::Imovel(int _andar, int _area) :andar(_andar), area(_area), codNum(++conta){}
int Imovel::getArea() const {return area;}
int Imovel::getAndar() const {return andar;}
int Imovel::getCodNum() const {return codNum;}
int Imovel::getPreco() const{return 0;}
string Imovel::getDescricao() const {return getCodigo()+ " Area: " + to_string(getArea()) + " Preco: " + to_string(getPreco()) +
            " Andar: " + to_string(getArea());}

string Imovel::getCodigo() const { return to_string(codNum);}