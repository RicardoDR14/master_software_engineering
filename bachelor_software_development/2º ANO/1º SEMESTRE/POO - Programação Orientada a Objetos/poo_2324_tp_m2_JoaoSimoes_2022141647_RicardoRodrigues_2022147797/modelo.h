
#ifndef TRABALHOPRATICOCODIGO_MODELO_H
#define TRABALHOPRATICOCODIGO_MODELO_H
#include "Terminal.h"
#include <vector>
#include <string>
#include "iteracao.h"

using namespace std;
using namespace term;

class Modelo {
public:
    Modelo();
    void run();
private:
    Terminal &terminal;
    Window JanelaGrafica;
    Window JanelaMensagens;
    Window JanelaComandos;
    Iteracao iteracao;
};


#endif //TRABALHOPRATICOCODIGO_MODELO_H
