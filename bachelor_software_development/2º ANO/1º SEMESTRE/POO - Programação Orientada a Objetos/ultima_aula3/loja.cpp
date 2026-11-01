//
// Created by riroc on 07/12/2023.
//
#include "loja.h"

int Loja::getPreco() const {return 15*getArea();}

string Loja::getCodigo() const {return "Loja"+ Imovel::getCodigo();}

Loja::Loja(int _area) : Imovel(0,_area){}

string Loja::getDescricao() {return getCodigo() + " Area: " + to_string(getArea()) + " Preco: " + to_string(getPreco()) + " Andar: " +
            to_string(getAndar());}