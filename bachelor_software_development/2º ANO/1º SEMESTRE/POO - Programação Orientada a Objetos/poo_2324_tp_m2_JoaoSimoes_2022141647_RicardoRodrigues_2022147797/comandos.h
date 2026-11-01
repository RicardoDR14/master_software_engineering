
#ifndef TRABALHOPRATICOCODIGO_COMANDOS_H
#define TRABALHOPRATICOCODIGO_COMANDOS_H

#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <string>
#include "iteracao.h"
using namespace std;

bool validarInteiro(const string& str);

void executarComando(Iteracao *iteracao,const string& keyword, vector<string> &parametros);

#endif //TRABALHOPRATICOCODIGO_COMANDOS_H
