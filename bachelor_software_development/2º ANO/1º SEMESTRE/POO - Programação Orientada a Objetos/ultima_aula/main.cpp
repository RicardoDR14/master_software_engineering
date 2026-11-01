#include <iostream>
#include "pessoa.h"
#include "RegCivil_v.h"
#include <vector>

int main() {

    RegCiv PT("Portugal");

    if (!PT.novaPessoa("Ricardo", 123, 123))
        cout << "Nao adicionou";
    else
        cout<<PT.listaPessoas();

    return 0;
}
