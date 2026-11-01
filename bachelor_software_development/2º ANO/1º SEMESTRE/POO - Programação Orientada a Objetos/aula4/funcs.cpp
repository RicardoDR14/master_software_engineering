#include "table.h"
#include <iostream>
#include <initializer_list>

using namespace std;

Tabela::Tabela() {
    preenche(1);
}

void Tabela::preenche(int val) {
    for (int i = 0; i < TAM; i++) {
        for (int j = 0; j < TAM; j++) {
            matriz[i][j] = val + (i * j);
        }
    }
}

void Tabela::imprime() const {
    vector<int> cont(matriz, matriz + TAM);
    span<int> impressao(matriz);
    for (int i :impressao)
            cout << i << "  ";
    cout << endl;
}

int Tabela::obtemEaltera(int a, int b, int c) {
    if (a < 0 || a >= TAM || b < 0 || b >= TAM) {
        cout << "Índice inválido. Tente novamente." << endl;
        return 1;
    } else {
        cout << "\nAntes: " << matriz[a][b] << endl;
        matriz[a][b] = c;
        cout << "Depois: " << matriz[a][b] << endl;
        imprime();
        return 0;
    }
}
