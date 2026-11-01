#include <iostream>
#include "aparelho.h"

using namespace std;

Aparelho::Aparelho(int _id) :aparelho_id(_id),isligado(false),temporizador(0){}
Aquecedor::Aquecedor(int _id): Aparelho(_id) {}
Aspersor::Aspersor(int _id) : Aparelho(_id) {}
Refrigerador::Refrigerador(int _id) : Aparelho(_id){}
Lampada::Lampada(int _id) : Aparelho(_id){}

int Aparelho::getId() const {return aparelho_id;}

bool Aparelho::getEstado() const {return isligado;}

void Aparelho::setEstado() {isligado=!isligado; temporizador=0;}

string Aquecedor::getNome() const {return "aquecedor";}

string Aspersor::getNome() const {return "aspersor";}

string Refrigerador::getNome() const {return "refrigerador";}

string Lampada::getNome() const {return "lampada";}

int Aparelho::getTemporizador() const {return temporizador;}

int Aparelho::incTemporizador() {temporizador++;}

