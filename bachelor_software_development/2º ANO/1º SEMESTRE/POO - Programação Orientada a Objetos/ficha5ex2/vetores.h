
#ifndef FICHA5EX2_VETORES_H
#define FICHA5EX2_VETORES_H

#include <iostream>
#include <string>
using namespace std;

class Vetor{
    double x, y;
public:
    Vetor(double cx, double cy);
    Vetor(double c);
    double getx() const;
    double gety() const;
    double setx(double nx);
    double sety(double ny);
    string descricao() const;
};


#endif //FICHA5EX2_VETORES_H
