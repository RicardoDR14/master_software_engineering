//
// Created by riroc on 23/11/2023.
//
#include <iostream>
#include "ponto.h"

using namespace std;
Ponto::Ponto(int cx, int cy) : x(cx), y(cy) {
    cout << "CONSTR. Ponto com " << x << "," << y << "\n";
}
Ponto::~Ponto(){
    cout << "DESTR. Ponto com " << x << "," << y << "\n";
}